package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.args.SetLeftPanelWidthArgs;
import erika.app.coffee.model.args.SetCurrentTableArgs;
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
            case ActionType.SET_CURRENT_TABLE:
                return setTable(state, (SetCurrentTableArgs) action);
            case ActionType.SET_LEFT_PANEL_WIDTH:
                return resizeLeftPanel(state, (SetLeftPanelWidthArgs) action);

            default:
                return state;
        }
    }

    private OrderState resizeLeftPanel(OrderState state, SetLeftPanelWidthArgs action) {
        Log.d("OrderReducer", "" + action.width);
        return Redux.copy(state, x -> {
            x.leftPanelWidth = action.width;
        });
    }

    private OrderState setTable(OrderState state, SetCurrentTableArgs action) {
        return Redux.copy(state, x -> {
            x.tableId = action.table.id;
            x.tableName = action.table.name;
        });
    }
}
