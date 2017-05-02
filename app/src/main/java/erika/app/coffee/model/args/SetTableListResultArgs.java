package erika.app.coffee.model.args;

import java.util.List;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.CheckableTable;
import erika.app.coffee.model.TableStatus;

public class SetTableListResultArgs extends Args {
    public final List<CheckableTable> items;

    public SetTableListResultArgs(List<CheckableTable> items) {
        super(ActionType.SET_TABLE_LIST_RESULT);
        this.items = items;
    }
}
