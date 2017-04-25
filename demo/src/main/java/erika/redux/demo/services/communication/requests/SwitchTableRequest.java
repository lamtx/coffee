package erika.redux.demo.services.communication.requests;

import erika.core.communication.Writer;

public class SwitchTableRequest extends Request {

    public int fromTable;
    public int toTable;

    public SwitchTableRequest(int fromTable, int toTable) {
        this.fromTable = fromTable;
        this.toTable = toTable;
    }

    @Override
    public void writeToWriter(Writer writer) {
        super.writeToWriter(writer);
        writer.write(fromTable).write(toTable);
    }
}
