package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.args.SetTableForOrderComponentArgs;
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
            case ActionType.SET_TABLE_FOR_ORDER_COMPONENT:
                return setTable(state, (SetTableForOrderComponentArgs) action);
            default:
                return state;
        }
    }

    private OrderState setTable(OrderState state, SetTableForOrderComponentArgs action) {
        return Redux.copy(state, x -> {
            x.tableId = action.table.id;
            x.tableName = action.table.name;
        });
    }
}
