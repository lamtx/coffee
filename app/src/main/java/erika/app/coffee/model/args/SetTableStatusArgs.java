package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.TableStatus;

public class SetTableStatusArgs extends Args {
    public final int tableId;
    public final TableStatus status;

    public SetTableStatusArgs(int tableId, TableStatus status) {
        super(ActionType.SET_TABLE_STATUS);
        this.tableId = tableId;
        this.status = status;
    }
}
