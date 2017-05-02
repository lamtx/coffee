package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.args.PushComponentArgs;
import erika.app.coffee.state.MainState;
import erika.core.redux.Action;
import erika.core.redux.Reducer;
import erika.core.redux.Redux;

public class MainReducer implements Reducer<MainState> {
    @NonNull
    @Override
    public MainState reduce(@Nullable MainState state, Action action) {
        if (state == null) {
            state = new MainState();
        }
        switch (action.getType()) {
            case ActionType.PUSH:
                return push(state, (PushComponentArgs) action);
            case ActionType.POP:
                return pop(state);
            default:
                return state;
        }
    }

    private MainState push(MainState state, PushComponentArgs args) {
        return Redux.copy(state, x -> {
            x.backStack = state.backStack.push(args.component);
        });
    }

    private MainState pop(MainState state) {
        return Redux.copy(state, x -> {
            x.backStack = state.backStack.pop();
        });
    }
}
