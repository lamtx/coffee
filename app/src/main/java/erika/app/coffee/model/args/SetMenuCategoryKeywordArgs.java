package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;

public class SetMenuCategoryKeywordArgs extends Args {
    public final String keyword;

    public SetMenuCategoryKeywordArgs(String keyword) {
        super(ActionType.SET_MENU_CATEGORY_KEYWORD);
        this.keyword = keyword;
    }
}
