package erika.app.coffee.action;

import android.content.Context;

import java.util.List;

import erika.app.coffee.model.LoadState;
import erika.app.coffee.model.args.SetIsRefreshingArgs;
import erika.app.coffee.model.args.SetLoadStateArgs;
import erika.app.coffee.model.args.SetOrderedMenuListArgs;
import erika.app.coffee.reducer.OrderedListReducer;
import erika.app.coffee.service.ServiceInterface;
import erika.app.coffee.service.communication.OrderedMenuItem;
import erika.core.redux.Action;
import erika.core.redux.DispatchAction;

public class OrderedListActions {
    public static DispatchAction fetchItems(Context context, int tableId) {
        return dispatcher -> {
            dispatcher.dispatch(setLoadState(LoadState.LOADING));
            ServiceInterface.shared(context).fetchOrderedMenuItems(tableId).then(task -> {
                dispatcher.dispatch(setIsRefreshing(false));
                if (task.isCompleted()) {
                    dispatcher.dispatch(setLoadState(LoadState.NONE));
                    dispatcher.dispatch(setItemsResult(task.getResult().tableId, task.getResult().menus, task.getResult().tableName));
                    dispatcher.dispatch(MainActions.setAppTitle(task.getResult().tableName, ""));
                } else {
                    dispatcher.dispatch(setLoadState(LoadState.FAILED));
                }
            });
        };
    }

    private static Action setItemsResult(int tableId, List<OrderedMenuItem> items, String tableName) {
        return new SetOrderedMenuListArgs(tableId, items, tableName);
    }

    public static Action setLoadState(LoadState loadState) {
        return new SetLoadStateArgs(OrderedListActions.class, loadState);
    }

    public static Action setIsRefreshing(boolean refreshing) {
        return new SetIsRefreshingArgs(OrderedListReducer.class, refreshing);
    }
}
