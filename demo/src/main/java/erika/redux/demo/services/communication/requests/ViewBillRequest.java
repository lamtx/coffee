package erika.redux.demo.services.communication.requests;


import erika.core.communication.Writer;

public class ViewBillRequest extends Request {
    public int tableId;

    public ViewBillRequest(int tableId) {
        this.tableId = tableId;
    }

    @Override
    public void writeToWriter(Writer writer) {
        super.writeToWriter(writer);
        writer.write(tableId);
    }
}
