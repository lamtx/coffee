package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.args.SetHostArgs;
import erika.app.coffee.model.args.SetPasswordArgs;
import erika.app.coffee.model.args.SetUserNameArgs;
import erika.app.coffee.state.SignInState;
import erika.core.redux.Action;
import erika.core.redux.Reducer;
import erika.core.redux.Redux;

public class SignInReducer implements Reducer<SignInState> {
    @NonNull
    @Override
    public SignInState reduce(@Nullable SignInState state, Action action) {
        if (state == null) {
            state = new SignInState();
        }
        switch (action.getType()) {
            case ActionType.SET_USER_NAME:
                return setUserName(state, (SetUserNameArgs) action);
            case ActionType.SET_PASSWORD:
                return setPassword(state, (SetPasswordArgs) action);
            case ActionType.SET_HOST:
                return setHost(state, (SetHostArgs) action);
            default:
                return state;
        }
    }

    private SignInState setUserName(SignInState state, SetUserNameArgs action) {
        return Redux.copy(state, x -> {
            x.userName = action.userName;
        });
    }

    private SignInState setPassword(SignInState state, SetPasswordArgs action) {
        return Redux.copy(state, x -> {
            x.password = action.password;
        });
    }

    private SignInState setHost(SignInState state, SetHostArgs action) {
        return Redux.copy(state, x -> {
            x.host = action.host;
        });
    }
}
