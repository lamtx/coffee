package erika.app.coffee.state;

import android.util.SparseArray;

import java.util.Collections;
import java.util.List;

import erika.app.coffee.model.LoadState;
import erika.app.coffee.service.communication.OrderedMenuItem;

public class OrderedListState extends BaseListState<OrderedMenuItem> {
    public int tableId;
    public boolean refreshing;
    public LoadState loadState;
    public SparseArray<List<OrderedMenuItem>> list = new SparseArray<>();

    @Override
    public List<OrderedMenuItem> getItems() {
        List<OrderedMenuItem> items = list.get(tableId);
        return items == null ? Collections.emptyList() : items;
    }
}
