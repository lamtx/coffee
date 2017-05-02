package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.args.ShowPopupArgs;
import erika.app.coffee.state.PopupState;
import erika.core.redux.Action;
import erika.core.redux.Reducer;
import erika.core.redux.Redux;

public class PopupReducer implements Reducer<PopupState> {
    @NonNull
    @Override
    public PopupState reduce(@Nullable PopupState state, Action action) {
        if (state == null) {
            state = new PopupState();
        }
        switch (action.getType()) {
            case ActionType.SHOW_POPUP:
                return showPopup(state, (ShowPopupArgs) action);
            case ActionType.DISMISS_POPUP:
                return dismissPopup(state);
            default:
                return state;
        }
    }

    private PopupState showPopup(PopupState state, ShowPopupArgs args) {
        return Redux.copy(state, x -> {
            x.popupContainer = args.popupContainer;
        });
    }

    private PopupState dismissPopup(PopupState state) {
        return Redux.copy(state, x -> {
            x.popupContainer = null;
        });
    }
}
