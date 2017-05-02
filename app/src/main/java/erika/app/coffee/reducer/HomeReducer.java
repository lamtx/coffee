package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.CheckableTable;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.model.args.SetCheckableTableCheckedArgs;
import erika.app.coffee.model.args.SetTableListResultArgs;
import erika.app.coffee.state.HomeState;
import erika.app.coffee.state.TableListState;
import erika.core.redux.Action;
import erika.core.redux.Reducer;
import erika.core.redux.Redux;

public class HomeReducer implements Reducer<HomeState> {
    @NonNull
    @Override
    public HomeState reduce(@Nullable HomeState state, Action action) {
        if (state == null) {
            state = new HomeState();
        }
        switch (action.getType()) {
            case ActionType.SET_CHECKABLE_TABLE_CHECKED:
                return setCheckableTableChecked(state, (SetCheckableTableCheckedArgs) action);
            case ActionType.SET_TABLE_LIST_RESULT:
                return setTableListResult(state, (SetTableListResultArgs) action);
            default:
                return state;
        }
    }

    private HomeState setCheckableTableChecked(HomeState state, SetCheckableTableCheckedArgs action) {
        return Redux.copy(state, x -> {
            TableListState childState = state.pages.get(action.receiver.ordinal());
            x.pages = x.pages.set(action.receiver.ordinal(), Redux.copy(childState, child -> {
                child.items = Redux.map(child.items, e -> {
                    if (e == action.item) {
                        return new CheckableTable(e.table, action.checked);
                    }
                    return e;
                });
            }));
        });
    }

    private HomeState setTableListResult(HomeState state, SetTableListResultArgs action) {
        return Redux.copy(state, x -> {
            TableListState childState = state.pages.get(action.receiver.ordinal());
            x.pages = x.pages.set(action.receiver.ordinal(), Redux.copy(childState, child -> {
                child.items = action.items;
                child.loadState = LoadState.NONE;
                child.refreshing = false;
            }));
        });
    }
}
