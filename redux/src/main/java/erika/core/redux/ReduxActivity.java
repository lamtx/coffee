package erika.core.redux;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import erika.core.redux.utils.StateBinder;

public abstract class ReduxActivity<AppState, State> extends Activity implements Component<AppState, State> {
    private State state;
    private final StateBinder<AppState> binder = new StateBinder<>();
    private boolean isAlive;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setState(getStateFromStore(getStore().getState()));
    }

    public Store<AppState> getStore() {
        ReduxApplication<AppState> application = (ReduxApplication<AppState>) getApplication();
        return application.getStore();

    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    @Override
    @CallSuper
    public void bindStateToView(State state) {
        binder.rebindState(getStore().getState());
    }

    @Override
    public abstract State getStateFromStore(AppState state);

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof Component) {
            binder.onComponentAdded((Component<AppState, ?>) fragment);
        }
    }

    public void onDetachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof Component) {
            binder.onComponentRemoved((Component<AppState, ?>) fragment);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isAlive = true;
        bindStateToView(getState());
    }

    @Override
    protected void onStop() {
        super.onStop();
        isAlive = false;
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }
}
