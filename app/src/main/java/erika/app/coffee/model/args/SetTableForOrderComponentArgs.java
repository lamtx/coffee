package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.service.communication.Table;

public class SetTableForOrderComponentArgs extends Args {

    public final Table table;
    public final boolean shouldReload;

    public SetTableForOrderComponentArgs(Table table, boolean shouldReload) {
        super(ActionType.SET_TABLE_FOR_ORDER_COMPONENT);
        this.table = table;
        this.shouldReload = shouldReload;
    }
}
