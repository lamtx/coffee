package erika.app.coffee.model.args;

import java.util.List;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.service.communication.MenuCategory;

public class SetMenuCategoryListArgs extends Args {
    public final List<MenuCategory> categoryList;
    public final List<MenuCategory> noneFilteredList;

    public SetMenuCategoryListArgs( List<MenuCategory> categoryList, List<MenuCategory> noneFilteredList) {
        super(ActionType.SET_MENU_CATEGORY_LIST);
        this.categoryList = categoryList;
        this.noneFilteredList = noneFilteredList;
    }
}
