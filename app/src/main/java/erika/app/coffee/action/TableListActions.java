package erika.app.coffee.action;

import erika.app.coffee.model.CheckableTable;
import erika.app.coffee.model.TableStatus;
import erika.app.coffee.model.args.SetCheckableTableCheckedArgs;
import erika.core.redux.Action;

public class TableListActions {
    public static Action setCheckableTableChecked(TableStatus receiver, CheckableTable item, boolean checked) {
        return new SetCheckableTableCheckedArgs(receiver, item, checked);
    }
}
