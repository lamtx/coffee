package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.MenuType;

public class SetMenuCategoryKeywordArgs extends Args {
    public final MenuType menuType;
    public final String keyword;

    public SetMenuCategoryKeywordArgs(MenuType menuType, String keyword) {
        super(ActionType.SET_MENU_CATEGORY_KEYWORD);
        this.menuType = menuType;
        this.keyword = keyword;
    }
}
