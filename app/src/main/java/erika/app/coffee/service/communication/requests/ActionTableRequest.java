package erika.app.coffee.service.communication.requests;

import erika.core.communication.Writer;

public class ActionTableRequest extends Request {
    public int actionTable;
    public int tableId;

    @Override
    public void writeToWriter(Writer writer) {
        super.writeToWriter(writer);
        writer.write(actionTable).write(tableId);
    }
}