package erika.app.coffee.action;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import erika.app.coffee.model.MenuType;
import erika.app.coffee.service.ServiceInterface;
import erika.app.coffee.service.communication.MenuCategory;
import erika.app.coffee.service.communication.MenuItem;
import erika.core.Vietnamese;
import erika.core.redux.DispatchAction;

public class MenuItemActions {
    public static DispatchAction fetchMenuItems(Context context, MenuType menuType, String keyword) {
        return dispatcher -> ServiceInterface.shared(context).fetchMenuItems(menuType).then(task -> {
            dispatcher.dispatch(OrderActions.setMenuCategoryList(menuType, filter(task.getResult().menu, keyword)));
        });
    }

    public static DispatchAction search(MenuType menuType, List<MenuCategory> categories, String keyword) {
        return dispatcher -> dispatcher.dispatch(OrderActions.setMenuCategoryList(menuType, filter(categories, keyword)));
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
                    if (Vietnamese.normal(item.name).contains(textSearch) || Vietnamese.normal(item.englishName).contains(textSearch)) {
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
