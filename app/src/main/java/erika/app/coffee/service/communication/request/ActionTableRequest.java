package erika.app.coffee.service.communication.request;

import erika.app.coffee.model.TableAction;
import erika.core.communication.Writer;

public class ActionTableRequest extends Request {
    @TableAction
    public int action;
    public int tableId;

    public ActionTableRequest(@TableAction int action, int tableId) {
        this.action = action;
        this.tableId = tableId;
    }
    @Override
    public void writeToWriter(Writer writer) {
        super.writeToWriter(writer);
        writer.write(action).write(tableId);
    }
}