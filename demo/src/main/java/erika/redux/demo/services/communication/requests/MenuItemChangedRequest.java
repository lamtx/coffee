package erika.redux.demo.services.communication.requests;

import erika.core.communication.Writer;

public class MenuItemChangedRequest extends Request {
    public int tableId;
    public int menuItemId;
    public int quantity;
    public String note;
    public int detailId;

    public MenuItemChangedRequest(int tableId, int menuItemId, int quantity,
            int detailId, String note) {
        this.menuItemId = menuItemId;
        this.tableId = tableId;
        this.detailId = detailId;
        this.quantity = quantity;
        this.note = note;
    }

    @Override
    public void writeToWriter(Writer writer) {
        super.writeToWriter(writer);
        writer.write(tableId).write(menuItemId).write(detailId).write(quantity)
                .write(note);
    }
}
