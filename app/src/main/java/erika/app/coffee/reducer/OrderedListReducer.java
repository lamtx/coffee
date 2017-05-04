package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.state.OrderedListState;
import erika.core.redux.Action;
import erika.core.redux.Reducer;

public class OrderedListReducer implements Reducer<OrderedListState> {
    @NonNull
    @Override
    public OrderedListState reduce(@Nullable OrderedListState state, Action action) {
        if (state == null) {
            state = new OrderedListState();
        }
        switch (action.getType()) {
            default:
                return state;
        }
    }
}
