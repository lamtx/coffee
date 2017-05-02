package erika.app.coffee.action;

import android.content.Context;

import java.util.List;

import erika.app.coffee.model.CheckableTable;
import erika.app.coffee.model.TableStatus;
import erika.app.coffee.model.args.SetTableListResultArgs;
import erika.app.coffee.service.ServiceInterface;
import erika.core.Arrays;
import erika.core.redux.Action;
import erika.core.redux.DispatchAction;

public class HomeActions {
    public static DispatchAction fetchTable(Context context, TableStatus tableStatus, boolean includeOutside, String keyword) {
        return dispatcher -> {
            ServiceInterface.shared(context).fetchTable(tableStatus, includeOutside, keyword).then(task -> {
                if (task.isCompleted()) {
                    List<CheckableTable> result = Arrays.map(task.getResult().tables, x -> new CheckableTable(x, false));
                    dispatcher.dispatch(setTableListResult(tableStatus, result));
                }
            });
        };
    }

    private static Action setTableListResult(TableStatus tableStatus, List<CheckableTable> items) {
        return new SetTableListResultArgs(tableStatus, items);
    }
}
