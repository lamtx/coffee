package erika.app.coffee.state;

import android.app.Fragment;

import erika.app.coffee.component.SignInFragment;
import erika.core.redux.immutable.ImmutableStack;

public class MainState implements Cloneable {
    public ImmutableStack<Class<? extends Fragment>> backStack = new ImmutableStack<>(SignInFragment.class);
}
