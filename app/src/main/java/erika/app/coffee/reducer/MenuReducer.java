package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.action.MenuActions;
import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.model.args.SetIsRefreshingArgs;
import erika.app.coffee.model.args.SetMenuCategoryKeywordArgs;
import erika.app.coffee.model.args.SetMenuCategoryListArgs;
import erika.app.coffee.model.args.SetTableForOrderComponentArgs;
import erika.app.coffee.state.MenuState;
import erika.core.redux.Action;
import erika.core.redux.Reducer;
import erika.core.redux.Redux;

public class MenuReducer implements Reducer<MenuState> {
    @NonNull
    @Override
    public MenuState reduce(@Nullable MenuState state, Action action) {
        if (state == null) {
            state = new MenuState();
        }
        switch (action.getType()) {
            case ActionType.SET_MENU_CATEGORY_LIST:
                return setMenuCategoryList(state, (SetMenuCategoryListArgs) action);
            case ActionType.SET_MENU_CATEGORY_KEYWORD:
                return setMenuCategoryKeyword(state, (SetMenuCategoryKeywordArgs) action);
            case ActionType.SET_IS_REFRESHING:
                return setIsRefreshing(state, (SetIsRefreshingArgs) action);
            case ActionType.SET_TABLE_FOR_ORDER_COMPONENT:
                return setTable(state, (SetTableForOrderComponentArgs) action);
            default:
                return state;
        }
    }

    private MenuState setMenuCategoryList(MenuState state, SetMenuCategoryListArgs action) {
        return Redux.copy(state, menu -> {
            menu.refreshing = false;
            menu.loadState = LoadState.NONE;
            menu.items = action.categoryList;
            menu.noneFilteredItems = action.noneFilteredList;
        });
    }

    private MenuState setMenuCategoryKeyword(MenuState state, SetMenuCategoryKeywordArgs action) {
        return Redux.copy(state, e -> {
            e.keyword = action.keyword;
        });
    }

    private MenuState setIsRefreshing(MenuState state, SetIsRefreshingArgs action) {
        if (action.target == MenuReducer.class) {
            return Redux.copy(state, e -> {
                e.refreshing = action.refreshing;
            });
        }
        return state;
    }

    private MenuState setTable(MenuState state, SetTableForOrderComponentArgs action) {
        return Redux.copy(state, x -> {
            x.tableId = action.table.id;
        });
    }
}
