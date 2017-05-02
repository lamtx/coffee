package erika.app.coffee.state;

import erika.app.coffee.model.MenuType;
import erika.core.redux.immutable.ImmutableArrayList;

public class OrderState implements Cloneable {
    public ImmutableArrayList<MenuState> menuStates = new ImmutableArrayList<>(MenuType.values().length, i -> new MenuState());
}
