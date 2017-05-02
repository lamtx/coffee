package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.CheckableTable;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.model.args.SetCheckableTableCheckedArgs;
import erika.app.coffee.model.args.SetTableListResultArgs;
import erika.app.coffee.state.TableListState;
import erika.core.redux.Action;
import erika.core.redux.Reducer;
import erika.core.redux.Redux;

public class TableListReducer implements Reducer<TableListState> {
    @NonNull
    @Override
    public TableListState reduce(@Nullable TableListState state, Action action) {
        if (state == null) {
            state = new TableListState();
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

    private TableListState setCheckableTableChecked(TableListState state, SetCheckableTableCheckedArgs action) {
        return Redux.copy(state, x -> {
            x.items = Redux.map(x.items, e -> {
                if (e == action.item) {
                    return new CheckableTable(e.table, action.checked);
                }
                return e;
            });
        });
    }

    private TableListState setTableListResult(TableListState state, SetTableListResultArgs action) {
        return Redux.copy(state, x -> {
            x.items = action.items;
            x.loadState = LoadState.NONE;
            x.refreshing = false;
        });
    }
}
