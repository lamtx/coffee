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
                    dispatcher.dispatch(setLoadState(LoadState.NONE));
                    dispatcher.dispatch(setItemsResult(task.getResult().tableId, task.getResult().menus, task.getResult().tableName));
                    dispatcher.dispatch(MainActions.setAppTitle(task.getResult().tableName, ""));
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

    public static DispatchAction clear(Context context, int tableId, int id, MenuItem menuItem) {
        return dispatcher -> {
            String message = context.getString(R.string.message_clear_ordered_item, menuItem.name);
            String title = context.getString(R.string.title_clear_ordered_item);
            dispatcher.dispatch(MessageBoxActions.ask(message, title, () -> {
                ServiceInterface.shared(context).editOrderedItem(menuItem, tableId, -10000, id).then(task -> {
                    if (task.isCompleted() && task.getResult().successful) {
                        dispatcher.dispatch(addItem(tableId, menuItem, -10000, id));
                    }
                });
            }));
        };
    }
}
