package erika.app.coffee.service.communication.requests;

import erika.core.communication.Writer;

public class MenuItemChangedRequest extends Request {
    public int tableId;
    public int menuItemId;
    public int quantity;
    public int detailId;

    public MenuItemChangedRequest(int tableId, int menuItemId, int quantity,
            int detailId) {
        this.menuItemId = menuItemId;
        this.tableId = tableId;
        this.detailId = detailId;
        this.quantity = quantity;
    }

    @Override
    public void writeToWriter(Writer writer) {
        super.writeToWriter(writer);
        writer.write(tableId).write(menuItemId).write(detailId).write(quantity);
    }
}
