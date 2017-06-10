package erika.app.coffee.action;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import erika.app.coffee.R;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.model.args.SetIsRefreshingArgs;
import erika.app.coffee.reducer.MenuReducer;
import erika.app.coffee.service.ServiceInterface;
import erika.app.coffee.service.communication.MenuCategory;
import erika.app.coffee.service.communication.MenuItem;
import erika.core.Vietnamese;
import erika.core.redux.Action;
import erika.core.redux.DispatchAction;

public class MenuActions {

    public static DispatchAction fetchMenuItems(Context context, String keyword) {
        return dispatcher -> {
            dispatcher.dispatch(setIsRefreshing(true));
            ServiceInterface.shared(context).fetchMenuItems().then(task -> {
                dispatcher.dispatch(setIsRefreshing(false));
                dispatcher.dispatch(OrderActions.setMenuCategoryList(filter(task.getResult().categories, keyword), task.getResult().categories));
            });
        };
    }

    public static DispatchAction search(List<MenuCategory> source, String keyword) {
        return dispatcher -> dispatcher.dispatch(OrderActions.setMenuCategoryList(filter(source, keyword), source));
    }

    public static DispatchAction order(Context context, MenuItem item, int tableId, double quantity) {
        return dispatcher -> {
            int messageId = MessageActions.obtainMessageId();
            String message = (quantity >= 0 ? "Thêm món " : "Bớt món ") + item.name;
            dispatcher.dispatch(MessageActions.addMessage(message, messageId, LoadState.LOADING));
            ServiceInterface.shared(context).orderItem(item, tableId, quantity).then(task -> {
                if (task.isCompleted() && task.getResult().successful) {
                    dispatcher.dispatch(MessageActions.changeMessageStatus(messageId, LoadState.NONE));
                    dispatcher.dispatch(OrderedListActions.addItem(tableId, item, quantity, 0));
                    dispatcher.dispatch(OrderedListActions.fetchItems(context, tableId));
                } else {
                    dispatcher.dispatch(MessageActions.changeMessageStatus(messageId, LoadState.FAILED));
                }
            });
        };
    }


    public static DispatchAction clear(Context context, int tableId, int id, MenuItem menuItem) {
        return dispatcher -> {
            String message = context.getString(R.string.message_clear_ordered_item, menuItem.name);
            String title = context.getString(R.string.title_clear_ordered_item);
            dispatcher.dispatch(MessageBoxActions.ask(message, title, () -> {
                ServiceInterface.shared(context).editOrderedItem(menuItem, tableId, -10000, id).then(task -> {
                    if (task.isCompleted() && task.getResult().successful) {
                        dispatcher.dispatch(OrderedListActions.addItem(tableId, menuItem, -10000, id));
                        dispatcher.dispatch(OrderedListActions.fetchItems(context, tableId));
                    }
                });
            }));
        };
    }

    private static List<MenuCategory> filter(List<MenuCategory> source, String keyword) {
        if (source == null) {
            source = Collections.emptyList();
        }
        if (TextUtils.isEmpty(keyword)) {
            return source;
        } else {
            String textSearch = Vietnamese.normal(keyword);
            ArrayList<MenuCategory> result = new ArrayList<>();

            for (MenuCategory category : source) {
                ArrayList<MenuItem> items = new ArrayList<>();
                for (MenuItem item : category.items) {
                    if (Vietnamese.normal(item.name).contains(textSearch)) {
                        items.add(item);
                    }
                }
                if (!items.isEmpty()) {
                    result.add(new MenuCategory(category.name, items));
                }
            }
            return result;
        }
    }

    public static Action setIsRefreshing(boolean refreshing) {
        return new SetIsRefreshingArgs(MenuReducer.class, refreshing);
    }

    public static DispatchAction showNumberDialog(Context context, MenuItem item, int tableId, boolean decreaseMode) {
        return dispatcher -> {
            String title = (decreaseMode ? "Bớt " : "Thêm ") + item.name;
            dispatcher.dispatch(NumberActions.show(title, decreaseMode, quantity -> {
                dispatcher.dispatch(order(context, item, tableId, quantity));
            }));
        };
    }
}
