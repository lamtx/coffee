package erika.app.coffee.action;

import java.util.List;

import erika.app.coffee.model.args.SetLeftPanelWidthArgs;
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

    public static Action setMenuCategoryList(List<MenuCategory> categories, List<MenuCategory> noneFilteredList) {
        return new SetMenuCategoryListArgs(categories, noneFilteredList);
    }

    public static Action setMenuCategoryKeyword(String keyword) {
        return new SetMenuCategoryKeywordArgs(keyword);
    }

    public static Action setLeftPanelWidth(float distance) {
        return new SetLeftPanelWidthArgs(distance);
    }
}
