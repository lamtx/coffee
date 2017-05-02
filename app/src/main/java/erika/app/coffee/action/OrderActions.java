package erika.app.coffee.action;

import java.util.List;

import erika.app.coffee.model.MenuType;
import erika.app.coffee.model.args.SetMenuCategoryKeywordArgs;
import erika.app.coffee.model.args.SetMenuCategoryListArgs;
import erika.app.coffee.model.args.SetTableForOrderComponentArgs;
import erika.app.coffee.service.communication.MenuCategory;
import erika.app.coffee.service.communication.Table;
import erika.core.redux.Action;

public class OrderActions {
    public static Action setTable(Table table, boolean shouldReload) {
        return new SetTableForOrderComponentArgs(table, shouldReload);
    }

    public static Action setMenuCategoryList(MenuType menuType, List<MenuCategory> categories) {
        return new SetMenuCategoryListArgs(menuType, categories);
    }

    public static Action setMenuCategoryKeyword(MenuType menuType, String keyword) {
        return new SetMenuCategoryKeywordArgs(menuType, keyword);
    }
}
