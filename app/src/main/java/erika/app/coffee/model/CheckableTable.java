package erika.app.coffee.model;

import erika.app.coffee.service.communication.Table;

public class CheckableTable {
    public Table table;
    public boolean checked;

    public CheckableTable() {
    }

    public CheckableTable(Table table, boolean checked) {
        this.table = table;
        this.checked = checked;
    }
}
