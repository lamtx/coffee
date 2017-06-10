package erika.app.coffee.state;

import android.app.Fragment;

import erika.app.coffee.component.SignInFragment;
import erika.app.coffee.model.BackStackElement;
import erika.app.coffee.service.Client;
import erika.core.redux.immutable.ImmutableStack;

public class MainState implements Cloneable {
    public ImmutableStack<BackStackElement> backStack = new ImmutableStack<>(new BackStackElement(SignInFragment.class, "Sign in"));
    public Class<? extends Fragment> popupContainer = null;
    public boolean cancellable = true;
    public String title;
    public String subtitle;
    public Client.Status clientStatus;
    public String clientMessage;
    public int tableId;
}
