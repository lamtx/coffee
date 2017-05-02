package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.CheckableTable;
import erika.app.coffee.model.TableStatus;

public class SetCheckableTableCheckedArgs extends Args {
    public final CheckableTable item;
    public final boolean checked;

    public SetCheckableTableCheckedArgs(CheckableTable item, boolean checked) {
        super(ActionType.SET_CHECKABLE_TABLE_CHECKED);
        this.item = item;
        this.checked = checked;
    }
}
