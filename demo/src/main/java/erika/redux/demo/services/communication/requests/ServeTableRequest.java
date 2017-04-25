package erika.redux.demo.services.communication.requests;


import erika.core.communication.Writer;

public class ServeTableRequest extends Request {
    private int tableId;
    private int customerId;

    public int getTableId() {
        return tableId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public ServeTableRequest(int tableId, int customerId) {
        this.tableId = tableId;
        this.customerId = customerId;
    }

    @Override
    public void writeToWriter(Writer writer) {
        super.writeToWriter(writer);
        writer.write(tableId).write(customerId);
    }
}
