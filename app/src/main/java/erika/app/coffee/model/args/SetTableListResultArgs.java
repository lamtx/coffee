package erika.app.coffee.model.args;

import java.util.List;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.service.communication.Table;

public class SetTableListResultArgs extends Args {
    public final List<Table> items;

    public SetTableListResultArgs(List<Table> items) {
        super(ActionType.SET_TABLE_LIST_RESULT);
        this.items = items;
    }
}
