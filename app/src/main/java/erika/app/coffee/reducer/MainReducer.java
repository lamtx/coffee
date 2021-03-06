package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.component.OrderFragment;
import erika.app.coffee.model.BackStackElement;
import erika.app.coffee.model.args.PushComponentArgs;
import erika.app.coffee.model.args.SetAppTitleArgs;
import erika.app.coffee.model.args.SetClientStatus;
import erika.app.coffee.model.args.SetRootArgs;
import erika.app.coffee.model.args.SetCurrentTableArgs;
import erika.app.coffee.model.args.SetTablePriceArgs;
import erika.app.coffee.model.args.ShowPopupArgs;
import erika.app.coffee.state.MainState;
import erika.app.coffee.utility.Utils;
import erika.core.redux.Action;
import erika.core.redux.Reducer;
import erika.core.redux.Redux;
import erika.core.redux.immutable.ImmutableStack;

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
            case ActionType.SET_ROOT:
                return setRoot(state, (SetRootArgs) action);
            case ActionType.SET_CLIENT_STATUS:
                return setClientStatus(state, (SetClientStatus) action);
            case ActionType.SET_TABLE_PRICE:
                return setTalePrice(state, (SetTablePriceArgs) action);
            case ActionType.SET_CURRENT_TABLE:
                return setTable(state, (SetCurrentTableArgs)action);
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

    private MainState setRoot(MainState state, SetRootArgs action) {
        return Redux.copy(state, x -> {
            x.backStack = new ImmutableStack<>(action.element);
        });
    }

    private MainState setClientStatus(MainState state, SetClientStatus action) {
        return Redux.copy(state, x -> {
            x.clientMessage = action.message;
            x.clientStatus = action.status;
        });
    }

    private MainState setTalePrice(MainState state, SetTablePriceArgs action) {
        if (state.backStack.peek().component == OrderFragment.class) {
            return Redux.copy(state, x -> {
                if (x.tableId == action.tableId) {
                    x.subtitle = Utils.stringFrom(action.price);
                }
            });
        }
        return state;
    }

    private MainState setTable(MainState state, SetCurrentTableArgs action) {
        return Redux.copy(state, x -> {
            x.tableId = action.table.id;
        });
    }

}
