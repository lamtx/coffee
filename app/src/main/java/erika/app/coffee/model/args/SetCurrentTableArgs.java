package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.service.communication.Table;

public class SetCurrentTableArgs extends Args {

    public final Table table;
    public final boolean shouldReload;

    public SetCurrentTableArgs(Table table, boolean shouldReload) {
        super(ActionType.SET_CURRENT_TABLE);
        this.table = table;
        this.shouldReload = shouldReload;
    }
}
