package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.service.communication.MenuItem;

public class AddOrderedItemArgs extends Args {
    public final int tableId;
    public final MenuItem item;
    public final double quantity;
    public final int id;

    public AddOrderedItemArgs(int tableId, MenuItem item, double quantity, int id) {
        super(ActionType.ADD_ORDERED_ITEM);
        this.tableId = tableId;
        this.item = item;
        this.quantity = quantity;
        this.id = id;
    }
}
