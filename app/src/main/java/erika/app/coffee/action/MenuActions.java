package erika.app.coffee.action;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import erika.app.coffee.model.Message;
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
            dispatcher.dispatch(MessageActions.addMessage("Thêm món " + item.name, messageId, Message.Status.PROCESSING));
            ServiceInterface.shared(context).orderItem(item, tableId, quantity).then(task -> {
                if (task.isCompleted() && task.getResult().successful) {
                    dispatcher.dispatch(MessageActions.changeMessageStatus(messageId, Message.Status.FINISHED));
                    dispatcher.dispatch(OrderedListActions.fetchItems(context, tableId));
                } else {
                    dispatcher.dispatch(MessageActions.changeMessageStatus(messageId, Message.Status.FAILED));
                }
            });
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

    public static DispatchAction showNumberDialog(Context context, MenuItem item, int tableId) {
        return dispatcher -> {
            dispatcher.dispatch(NumberActions.show(quantity -> {
                dispatcher.dispatch(order(context, item, tableId, quantity));
            }));
        };
    }
}
