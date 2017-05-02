package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.args.SetMessageBoxArgs;
import erika.app.coffee.state.MessageBoxState;
import erika.core.redux.Action;
import erika.core.redux.Reducer;

public class MessageBoxReducer implements Reducer<MessageBoxState> {
    @NonNull
    @Override
    public MessageBoxState reduce(@Nullable MessageBoxState state, Action action) {
        if (state == null) {
            state = new MessageBoxState();
        }
        switch (action.getType()) {
            case ActionType.SET_MESSAGE_BOX:
                return ((SetMessageBoxArgs) action).state;
            default:
                return state;
        }
    }
}
