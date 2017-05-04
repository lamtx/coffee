package erika.app.coffee.service.communication.requests;

import erika.app.coffee.model.TableAction;
import erika.core.communication.Writer;

public class ActionTableRequest extends Request {
    @TableAction
    public int action;
    public int tableId;

    @Override
    public void writeToWriter(Writer writer) {
        super.writeToWriter(writer);
        writer.write(action).write(tableId);
    }
}