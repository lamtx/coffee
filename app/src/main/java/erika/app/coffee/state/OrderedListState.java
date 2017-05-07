package erika.app.coffee.state;

import android.util.SparseArray;

import java.util.List;

import erika.app.coffee.model.LoadState;
import erika.app.coffee.service.communication.OrderedMenuItem;

public class OrderedListState implements Cloneable {
    public int tableId;
    public boolean refreshing;
    public LoadState loadState;
    public SparseArray<List<OrderedMenuItem>> list = new SparseArray<>();
}
