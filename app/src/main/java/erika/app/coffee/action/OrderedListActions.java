package erika.app.coffee.action;

import android.content.Context;

import java.util.List;

import erika.app.coffee.R;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.model.args.AddOrderedItemArgs;
import erika.app.coffee.model.args.SetIsRefreshingArgs;
import erika.app.coffee.model.args.SetLoadStateArgs;
import erika.app.coffee.model.args.SetOrderedMenuListArgs;
import erika.app.coffee.reducer.OrderedListReducer;
import erika.app.coffee.service.ServiceInterface;
import erika.app.coffee.service.communication.MenuItem;
import erika.app.coffee.service.communication.OrderedMenuItem;
import erika.app.coffee.utility.Utils;
import erika.core.redux.Action;
import erika.core.redux.DispatchAction;

public class OrderedListActions {
    public static DispatchAction fetchItems(Context context, int tableId) {
        return dispatcher -> {
            dispatcher.dispatch(setIsRefreshing(true));
            dispatcher.dispatch(setLoadState(LoadState.LOADING));
            ServiceInterface.shared(context).fetchOrderedMenuItems(tableId).then(task -> {
                dispatcher.dispatch(setIsRefreshing(false));
                if (task.isCompleted()) {
                    double total = Utils.calcTotal(task.getResult().menus);
                    dispatcher.dispatch(setLoadState(LoadState.NONE));
                    dispatcher.dispatch(setItemsResult(task.getResult().tableId, task.getResult().menus, task.getResult().tableName));
                    dispatcher.dispatch(TableListActions.setTablePrice(tableId, total));
                } else {
                    dispatcher.dispatch(setLoadState(LoadState.FAILED));
                }
            });
        };
    }

    public static Action addItem(int tableId, MenuItem item, double quantity, int id) {
        return new AddOrderedItemArgs(tableId, item, quantity, id);
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
