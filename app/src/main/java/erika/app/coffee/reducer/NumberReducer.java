package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.args.SetNumberActionArgs;
import erika.app.coffee.model.args.SetNumberStateArgs;
import erika.app.coffee.model.args.SetNumberValueArgs;
import erika.app.coffee.state.NumberState;
import erika.core.redux.Action;
import erika.core.redux.Reducer;
import erika.core.redux.Redux;

public class NumberReducer implements Reducer<NumberState> {
    @NonNull
    @Override
    public NumberState reduce(@Nullable NumberState state, Action action) {
        if (state == null) {
            state = new NumberState();
        }
        switch (action.getType()) {
            case ActionType.SET_NUMBER_VALUE:
                return setNumberValue(state, (SetNumberValueArgs) action);
            case ActionType.SET_NUMBER_ACTION:
                return setNumberAction(state, (SetNumberActionArgs) action);
            case ActionType.SET_NUMBER_STATE:
                return setNumberState(state, (SetNumberStateArgs) action);
            default:
                return state;
        }
    }

    private NumberState setNumberAction(NumberState state, SetNumberActionArgs action) {
        return Redux.copy(state, x -> {
            x.action = action.action;
        });
    }

    private NumberState setNumberValue(NumberState state, SetNumberValueArgs action) {
        return Redux.copy(state, x -> {
            x.value = action.value;
        });
    }

    private NumberState setNumberState(NumberState state, SetNumberStateArgs action) {
        return Redux.copy(state, x -> {
            x.decreaseMode = action.decreaseMode;
            x.title = action.title;
        });
    }

}
