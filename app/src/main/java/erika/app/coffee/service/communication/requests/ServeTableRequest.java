package erika.app.coffee.service.communication.requests;

import erika.core.communication.Writer;

public class ServeTableRequest extends Request {
    public int tableId;

    public ServeTableRequest(int tableId) {
        this.tableId = tableId;
    }

    @Override
    public void writeToWriter(Writer writer) {
        super.writeToWriter(writer);
        writer.write(tableId);
    }
}
