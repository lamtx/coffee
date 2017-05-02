package erika.app.coffee.state;

import erika.app.coffee.model.TableStatus;
import erika.core.redux.immutable.ImmutableArrayList;

public class HomeState implements Cloneable {
    public ImmutableArrayList<TableListState> pages = new ImmutableArrayList<>(TableStatus.values().length,
            x -> new TableListState());
}
