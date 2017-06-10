package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;

public class SetTablePriceArgs extends Args {
    public final int tableId;
    public final double price;

    public SetTablePriceArgs(int tableId, double price) {
        super(ActionType.SET_TABLE_PRICE);
        this.tableId = tableId;
        this.price = price;
    }
}
