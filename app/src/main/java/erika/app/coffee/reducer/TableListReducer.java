package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.action.TableListActions;
import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.model.args.SetIsRefreshingArgs;
import erika.app.coffee.model.args.SetLoadStateArgs;
import erika.app.coffee.model.args.SetTableListResultArgs;
import erika.app.coffee.model.args.SetTableStatus;
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
            case ActionType.SET_TABLE_LIST_RESULT:
                return setTableListResult(state, (SetTableListResultArgs) action);
            case ActionType.SET_IS_REFRESHING:
                return setIsRefreshing(state, (SetIsRefreshingArgs) action);
            case ActionType.SET_LOAD_STATE:
                return setLoadState(state, (SetLoadStateArgs) action);
            case ActionType.SET_TABLE_STATUS:
                return setTableStatus(state, (SetTableStatus) action);
            default:
                return state;
        }
    }

    private TableListState setLoadState(TableListState state, SetLoadStateArgs action) {
        if (action.callingAction == TableListActions.class) {
            return Redux.copy(state, x -> {
                x.loadState = action.loadState;
            });
        }
        return state;
    }

    private TableListState setIsRefreshing(TableListState state, SetIsRefreshingArgs action) {
        if (action.target == TableListReducer.class) {
            return Redux.copy(state, x -> {
                x.refreshing = action.refreshing;
            });
        }
        return state;
    }

    private TableListState setTableListResult(TableListState state, SetTableListResultArgs action) {
        return Redux.copy(state, x -> {
            x.items = action.items;
            x.loadState = LoadState.NONE;
            x.refreshing = false;
        });
    }


    private TableListState setTableStatus(TableListState state, SetTableStatus action) {
        return Redux.copy(state, x-> {
            x.items = Redux.map(x.items, item -> {
               if (item.id == action.tableId) {
                   return Redux.copy(item, i -> {
                       i.status = action.status;
                   });
               }
               return item;
            });
        });
    }
}
