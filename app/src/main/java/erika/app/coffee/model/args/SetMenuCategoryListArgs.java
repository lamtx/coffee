package erika.app.coffee.model.args;

import java.util.List;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.MenuType;
import erika.app.coffee.service.communication.MenuCategory;

public class SetMenuCategoryListArgs extends Args {
    public final List<MenuCategory> categoryList;
    public final MenuType menuType;

    public SetMenuCategoryListArgs(MenuType menuType, List<MenuCategory> categoryList) {
        super(ActionType.SET_MENU_CATEGORY_LIST);
        this.categoryList = categoryList;
        this.menuType = menuType;
    }
}
