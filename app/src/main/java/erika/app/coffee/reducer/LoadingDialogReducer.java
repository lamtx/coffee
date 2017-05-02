package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.args.SetLoadingMessageArgs;
import erika.app.coffee.state.LoadingDialogState;
import erika.core.redux.Action;
import erika.core.redux.Reducer;
import erika.core.redux.Redux;

public class LoadingDialogReducer implements Reducer<LoadingDialogState> {

    @NonNull
    @Override
    public LoadingDialogState reduce(@Nullable LoadingDialogState state, Action action) {
        if (state == null) {
            state = new LoadingDialogState();
        }
        switch (action.getType()) {
            case ActionType.SET_LOADING_DIALOG_MESSAGE:
                return setLoadingMessage(state, (SetLoadingMessageArgs) action);
            default:
                return state;
        }
    }

    private LoadingDialogState setLoadingMessage(LoadingDialogState state, SetLoadingMessageArgs action) {
        return Redux.copy(state, x -> {
            x.message = action.message;
        });
    }
}
