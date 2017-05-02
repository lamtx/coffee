package erika.app.coffee.service.communication.requests;

import erika.core.communication.Writer;

public class ListOfOrderedMenuRequest extends Request {
    private int tableId;

    public ListOfOrderedMenuRequest(int tableId) {
        this.tableId = tableId;
    }

    @Override
    public void writeToWriter(Writer writer) {
        super.writeToWriter(writer);
        writer.write(tableId);
    }
}
