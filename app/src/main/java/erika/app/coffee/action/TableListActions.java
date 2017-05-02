package erika.app.coffee.action;

import android.content.Context;

import java.util.List;

import erika.app.coffee.model.CheckableTable;
import erika.app.coffee.model.TableStatus;
import erika.app.coffee.model.args.SetCheckableTableCheckedArgs;
import erika.app.coffee.model.args.SetTableListResultArgs;
import erika.app.coffee.service.ServiceInterface;
import erika.core.Arrays;
import erika.core.redux.Action;
import erika.core.redux.DispatchAction;

public class TableListActions {
    public static DispatchAction fetchTable(Context context) {
        return dispatcher -> {
            ServiceInterface.shared(context).fetchTable().then(task -> {
                if (task.isCompleted()) {
                    List<CheckableTable> result = Arrays.map(task.getResult().tables, x -> new CheckableTable(x, false));
                    dispatcher.dispatch(setTableListResult(result));
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
}
