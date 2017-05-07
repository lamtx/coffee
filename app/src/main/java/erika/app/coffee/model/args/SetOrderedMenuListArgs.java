package erika.app.coffee.model.args;


import java.util.List;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.service.communication.OrderedMenuItem;

public class SetOrderedMenuListArgs extends Args {
    public final List<OrderedMenuItem> items;
    public final String tableName;
    public final int tableId;
    public SetOrderedMenuListArgs(int tableId, List<OrderedMenuItem> items, String tableName) {
        super(ActionType.SET_ORDERED_MENU_LIST);
        this.tableId = tableId;
        this.items = items;
        this.tableName = tableName;
    }
}
