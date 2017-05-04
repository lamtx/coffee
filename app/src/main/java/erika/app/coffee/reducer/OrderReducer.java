package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.action.MenuItemActions;
import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.model.args.SetIsRefreshingArgs;
import erika.app.coffee.model.args.SetMenuCategoryKeywordArgs;
import erika.app.coffee.model.args.SetMenuCategoryListArgs;
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
            case ActionType.SET_IS_REFRESHING:
                return setIsRefreshing(state, (SetIsRefreshingArgs) action);
            default:
                return state;
        }
    }

    private OrderState setMenuCategoryList(OrderState state, SetMenuCategoryListArgs action) {
        return Redux.copy(state, x -> {
            x.menuState = Redux.copy(x.menuState, menu -> {
                menu.refreshing = false;
                menu.loadState = LoadState.NONE;
                menu.items = action.categoryList;
                menu.noneFilteredItems = action.noneFilteredList;
            });
        });
    }

    private OrderState setMenuCategoryKeyword(OrderState state, SetMenuCategoryKeywordArgs action) {
        return Redux.copy(state, x -> {
            x.menuState = Redux.copy(x.menuState, e -> {
                e.keyword = action.keyword;
            });
        });
    }

    private OrderState setIsRefreshing(OrderState state, SetIsRefreshingArgs action) {
        if (action.callingAction == MenuItemActions.class) {
            return Redux.copy(state, x -> {
                x.menuState = Redux.copy(x.menuState, e -> {
                    e.refreshing = action.refreshing;
                });
            });
        }
        return state;
    }

}
