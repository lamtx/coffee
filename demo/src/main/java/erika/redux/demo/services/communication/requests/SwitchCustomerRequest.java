package erika.redux.demo.services.communication.requests;


import erika.core.communication.Writer;

public class SwitchCustomerRequest extends Request {
    public int tableId;
    public int customerId;

    public SwitchCustomerRequest(int tableId, int customerId) {
        this.tableId = tableId;
        this.customerId = customerId;
    }

    @Override
    public void writeToWriter(Writer writer) {
        super.writeToWriter(writer);
        writer.write(tableId).write(customerId);
    }

}
