package erika.redux.demo.services.communication.requests;

import erika.core.communication.Writer;

public class SplitOrderedMenuRequest extends Request {

    public int orderedMenuId;
    public int quantity;
    public int fromTable;
    public int toTable;
    public int detailId;

    public SplitOrderedMenuRequest(int orderedMenuId, int detailId,
            int quantity, int fromTable, int toTable) {
        this.orderedMenuId = orderedMenuId;
        this.detailId = detailId;
        this.quantity = quantity;
        this.fromTable = fromTable;
        this.toTable = toTable;
    }

    @Override
    public void writeToWriter(Writer writer) {
        super.writeToWriter(writer);
        writer.write(orderedMenuId).write(detailId).write(quantity).write(fromTable)
                .write(toTable);
    }

}
