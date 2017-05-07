package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.BackStackElement;
import erika.app.coffee.model.args.PushComponentArgs;
import erika.app.coffee.model.args.SetAppTitleArgs;
import erika.app.coffee.model.args.ShowPopupArgs;
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
            case ActionType.SHOW_POPUP:
                return showPopup(state, (ShowPopupArgs) action);
            case ActionType.DISMISS_POPUP:
                return dismissPopup(state);
            case ActionType.SET_APP_TITLE:
                return setAppTitle(state, (SetAppTitleArgs) action);
            default:
                return state;
        }
    }

    private MainState push(MainState state, PushComponentArgs args) {
        return Redux.copy(state, x -> {
            x.backStack = state.backStack.push(args.element);
            x.title = args.element.title;
            x.subtitle = args.element.subtitle;
        });
    }

    private MainState pop(MainState state) {
        return Redux.copy(state, x -> {
            x.backStack = state.backStack.pop();
            if (!x.backStack.isEmpty()) {
                BackStackElement top = x.backStack.peek();
                x.subtitle = top.subtitle;
                x.title = top.title;
            }
        });
    }

    private MainState showPopup(MainState state, ShowPopupArgs args) {
        return Redux.copy(state, x -> {
            x.popupContainer = args.popupContainer;
        });
    }

    private MainState dismissPopup(MainState state) {
        return Redux.copy(state, x -> {
            x.popupContainer = null;
        });
    }

    private MainState setAppTitle(MainState state, SetAppTitleArgs action) {
        return Redux.copy(state, x -> {
            x.title = action.title;
            x.subtitle = action.subtitle;
        });
    }
}
