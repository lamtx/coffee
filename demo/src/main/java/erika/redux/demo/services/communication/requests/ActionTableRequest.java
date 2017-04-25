package erika.redux.demo.services.communication.requests;

import erika.core.communication.Writer;

public class ActionTableRequest extends Request {
    public int actionTable;
    public String actionMessage;
    public int tableId;
    public int paid;
    public int extraPaid;

    public ActionTableRequest(int actionTable, int tableId) {
        this.actionTable = actionTable;
        this.tableId = tableId;
        this.paid = 0;
        this.actionMessage = null;
        this.extraPaid = 0;
    }

    public ActionTableRequest(int actionTable, int tableId, String msg) {
        this.actionTable = actionTable;
        this.tableId = tableId;
        this.paid = 0;
        this.actionMessage = msg;
        this.extraPaid = 0;
    }

    public ActionTableRequest(int actionTable, int tableId, int paid, int extraPaid) {
        this.actionTable = actionTable;
        this.tableId = tableId;
        this.paid = paid;
        this.actionMessage = null;
        this.extraPaid = extraPaid;
    }

    @Override
    public void writeToWriter(Writer writer) {
        super.writeToWriter(writer);
        writer.write(actionTable).write(tableId).write(paid).write(extraPaid).write(actionMessage);
    }
}