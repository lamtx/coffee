package erika.app.coffee.state;

import erika.app.coffee.model.CheckableTable;

public class TableListState extends BaseListState<CheckableTable> {
    public boolean showCustomer = true;
    public boolean isMultiSelection = true;
    public boolean enableLongClick = true;
}
