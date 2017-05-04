package erika.app.coffee.action;

import android.content.Context;

import java.util.List;

import erika.app.coffee.model.CheckableTable;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.model.args.SetCheckableTableCheckedArgs;
import erika.app.coffee.model.args.SetIsRefreshingArgs;
import erika.app.coffee.model.args.SetLoadStateArgs;
import erika.app.coffee.model.args.SetTableListResultArgs;
import erika.app.coffee.service.ServiceInterface;
import erika.core.Arrays;
import erika.core.redux.Action;
import erika.core.redux.DispatchAction;

public class TableListActions {
    public static DispatchAction fetchTable(Context context) {
        return dispatcher -> {
            dispatcher.dispatch(setLoadState(LoadState.LOADING));
            ServiceInterface.shared(context).fetchTable().then(task -> {
                dispatcher.dispatch(setIsRefreshing(false));
                if (task.isCompleted()) {
                    dispatcher.dispatch(setLoadState(LoadState.NONE));
                    List<CheckableTable> result = Arrays.map(task.getResult().tables, x -> new CheckableTable(x, false));
                    dispatcher.dispatch(setTableListResult(result));
                } else {
                    dispatcher.dispatch(setLoadState(LoadState.FAILED));
                }
            });
        };
    }

    private static Action setTableListResult(List<CheckableTable> items) {
        return new SetTableListResultArgs(items);
    }

    public static Action setCheckableTableChecked(CheckableTable item, boolean checked) {
        return new SetCheckableTableCheckedArgs(item, checked);
    }

    public static Action setLoadState(LoadState loadState) {
        return new SetLoadStateArgs(TableListActions.class, loadState);
    }

    public static Action setIsRefreshing(boolean refreshing) {
        return new SetIsRefreshingArgs(TableListActions.class, refreshing);
    }
}
