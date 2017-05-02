package erika.app.coffee.action;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import erika.app.coffee.service.ServiceInterface;
import erika.app.coffee.service.communication.MenuCategory;
import erika.app.coffee.service.communication.MenuItem;
import erika.core.Vietnamese;
import erika.core.redux.DispatchAction;

public class MenuItemActions {

    public static DispatchAction fetchMenuItems(Context context, String keyword) {
        return dispatcher -> ServiceInterface.shared(context).fetchMenuItems().then(task -> {
            dispatcher.dispatch(OrderActions.setMenuCategoryList(filter(task.getResult().categories, keyword), task.getResult().categories));
        });
    }

    public static DispatchAction search(List<MenuCategory> source, String keyword) {
        return dispatcher -> dispatcher.dispatch(OrderActions.setMenuCategoryList(filter(source, keyword), source));
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
}
