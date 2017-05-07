package erika.app.coffee.service.communication.requests;

import erika.core.communication.Writer;

public class MenuItemChangedRequest extends Request {
    public int tableId;
    public int menuItemId;
    public double quantity;
    public int id;

    public MenuItemChangedRequest(int tableId, int menuItemId, double quantity, int id) {
        this.menuItemId = menuItemId;
        this.tableId = tableId;
        this.id = id;
        this.quantity = quantity;
    }

    @Override
    public void writeToWriter(Writer writer) {
        super.writeToWriter(writer);
        writer.write(tableId).write(menuItemId).write(id).write(quantity);
    }
}
