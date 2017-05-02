package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.model.args.SetMenuCategoryKeywordArgs;
import erika.app.coffee.model.args.SetMenuCategoryListArgs;
import erika.app.coffee.state.MenuState;
import erika.app.coffee.state.OrderState;
import erika.core.redux.Action;
import erika.core.redux.Reducer;
import erika.core.redux.Redux;

public class OrderReducer implements Reducer<OrderState> {
    @NonNull
    @Override
    public OrderState reduce(@Nullable OrderState state, Action action) {
        if (state == null) {
            state = new OrderState();
        }
        switch (action.getType()) {
            case ActionType.SET_MENU_CATEGORY_LIST:
                return setMenuCategoryList(state, (SetMenuCategoryListArgs) action);
            case ActionType.SET_MENU_CATEGORY_KEYWORD:
                return setMenuCategoryKeyword(state, (SetMenuCategoryKeywordArgs) action);
            default:
                return state;
        }
    }

    private OrderState setMenuCategoryList(OrderState state, SetMenuCategoryListArgs action) {
        return Redux.copy(state, x -> {
            MenuState menuState = x.menuStates.get(action.menuType.ordinal());
            x.menuStates = x.menuStates.set(action.menuType.ordinal(), Redux.copy(menuState, menu -> {
                menu.refreshing = false;
                menu.loadState = LoadState.NONE;
                menu.items = action.categoryList;
            }));
        });
    }

    private OrderState setMenuCategoryKeyword(OrderState state, SetMenuCategoryKeywordArgs action) {
        return Redux.copy(state, x -> {
            MenuState menuState = x.menuStates.get(action.menuType.ordinal());
            x.menuStates = x.menuStates.set(action.menuType.ordinal(), Redux.copy(menuState, menu -> {
                menu.keyword = action.keyword;
            }));
        });
    }
}
