package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import erika.app.coffee.action.OrderedListActions;
import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.model.args.AddOrderedItemArgs;
import erika.app.coffee.model.args.SetIsRefreshingArgs;
import erika.app.coffee.model.args.SetLoadStateArgs;
import erika.app.coffee.model.args.SetOrderedMenuListArgs;
import erika.app.coffee.model.args.SetTableForOrderComponentArgs;
import erika.app.coffee.service.communication.OrderedMenuItem;
import erika.app.coffee.state.OrderedListState;
import erika.core.Arrays;
import erika.core.Predicate;
import erika.core.redux.Action;
import erika.core.redux.Reducer;
import erika.core.redux.Redux;

public class OrderedListReducer implements Reducer<OrderedListState> {
    @NonNull
    @Override
    public OrderedListState reduce(@Nullable OrderedListState state, Action action) {
        if (state == null) {
            state = new OrderedListState();
        }
        switch (action.getType()) {
            case ActionType.SET_TABLE_FOR_ORDER_COMPONENT:
                return setTable(state, (SetTableForOrderComponentArgs) action);
            case ActionType.SET_ORDERED_MENU_LIST:
                return setOrderedMenuList(state, (SetOrderedMenuListArgs) action);
            case ActionType.SET_IS_REFRESHING:
                return setIsRefreshing(state, (SetIsRefreshingArgs) action);
            case ActionType.SET_LOAD_STATE:
                return setLoadState(state, (SetLoadStateArgs) action);
            case ActionType.ADD_ORDERED_ITEM:
                return addOrderedItem(state, (AddOrderedItemArgs) action);
            default:
                return state;
        }
    }

    private OrderedListState setTable(OrderedListState state, SetTableForOrderComponentArgs action) {
        return Redux.copy(state, x -> {
            x.tableId = action.table.id;
        });
    }

    private OrderedListState setLoadState(OrderedListState state, SetLoadStateArgs action) {
        if (action.callingAction == OrderedListActions.class) {
            return Redux.copy(state, x -> {
                x.loadState = action.loadState;
            });
        }
        return state;
    }

    private OrderedListState setIsRefreshing(OrderedListState state, SetIsRefreshingArgs action) {
        if (action.target == OrderedListReducer.class) {
            return Redux.copy(state, x -> {
                x.refreshing = action.refreshing;
            });
        }
        return state;
    }


    private OrderedListState setOrderedMenuList(OrderedListState state, SetOrderedMenuListArgs action) {
        return Redux.copy(state, x -> {
            x.list = Redux.put(x.list, action.tableId, action.items);
            x.loadState = LoadState.NONE;
            x.refreshing = false;
        });
    }

    private OrderedListState addOrderedItem(OrderedListState state, AddOrderedItemArgs action) {
        List<OrderedMenuItem> items = state.list.get(action.tableId);
        List<OrderedMenuItem> result;
        if (action.id == 0) {
            int index = Arrays.indexOf(items, (Predicate<OrderedMenuItem>) x -> x.menuItem.id == action.item.id);
            if (index == -1) {
                result = Redux.add(items, new OrderedMenuItem(action.item, action.quantity, action.id));
            } else {
                result = Redux.set(items, index, Redux.copy(items.get(index), x-> {
                    x.quantity = Math.max(0, x.quantity + action.quantity);
                }));
            }
        } else {
            int index = Arrays.indexOf(items, (Predicate<OrderedMenuItem>) x -> x.id == action.id);
            if (index != -1) {
                OrderedMenuItem orderedMenuItem = items.get(index);
                if (orderedMenuItem.quantity + action.quantity <= 0) {
                    result = Redux.remove(items, index);
                } else {
                    result = Redux.set(items, index, Redux.copy(orderedMenuItem, x -> {
                        x.quantity = Math.max(0, x.quantity + action.quantity);
                    }));
                }
            } else {
                result = Redux.add(items, new OrderedMenuItem(action.item, action.quantity, action.id));
            }
        }
        return Redux.copy(state, x -> {
           x.list = Redux.put(x.list, action.tableId, result);
        });
    }
}
