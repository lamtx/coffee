package erika.app.coffee.state;

import java.util.Collections;
import java.util.List;

import erika.app.coffee.model.LoadState;

public class BaseListState<T> implements Cloneable {
    public List<T> items = Collections.emptyList();
    public LoadState loadState = LoadState.NONE;
    public boolean refreshing = false;

    public List<T> getItems() {
        return items;
    }
}
