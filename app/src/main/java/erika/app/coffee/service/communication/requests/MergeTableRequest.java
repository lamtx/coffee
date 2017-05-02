package erika.app.coffee.service.communication.requests;

import erika.app.coffee.service.communication.Table;
import erika.core.communication.Writer;

public class MergeTableRequest extends Request {

    public int[] tables;
    public int mergeTo;

    public MergeTableRequest(int[] tables, int mergeTo) {
        this.tables = tables;
        this.mergeTo = mergeTo;
    }

    public MergeTableRequest(Table[] fromTables, Table toTable) {
        tables = new int[fromTables.length];
        for (int i = 0; i < fromTables.length; i++) {
            tables[i] = fromTables[i].id;
        }
        mergeTo = toTable.id;
    }

    @Override
    public void writeToWriter(Writer writer) {
        super.writeToWriter(writer);
        writer.write(mergeTo).write(tables);
    }
}
