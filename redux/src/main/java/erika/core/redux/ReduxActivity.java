package erika.core.redux;

import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;

public abstract class ReduxActivity<AppState, State> extends AppCompatActivity implements Component<AppState, State> {
    private State state;

    private Store<AppState> getStore() {
        @SuppressWarnings({"unchecked"})
        ReduxApplication<AppState> application = (ReduxApplication<AppState>) getApplication();
        return application.getStore();

    }

    @Override
    public State getState() {
        return state;
    }

    private void setState(State state) {
        this.state = state;
    }

    @Override
    @CallSuper
    public void bindStateToView(State state) {
    }

    @Override
    public abstract State getStateFromStore(AppState state);

    @CallSuper
    @Override
    public void willReceiveState(State state) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        getStore().registerStateChangedListener(onStateChangedListener);
        rebindState(getStore().getState());
    }

    @Override
    protected void onStop() {
        super.onStop();
        getStore().unregisterStateChangedListener(onStateChangedListener);
    }

    private void rebindState(AppState newAppState) {
        State oldState = getState();
        State newState = getStateFromStore(newAppState);
        if (newState != oldState) {
            willReceiveState(newState);
            setState(newState);
            bindStateToView(newState);
        }
    }

    public void dispatch(Action action) {
        getStore().dispatch(action);
    }

    public void dispatch(DispatchAction action) {
        getStore().dispatch(action);
    }

    private final Store.OnStateChangedListener<AppState> onStateChangedListener = new Store.OnStateChangedListener<AppState>() {
        @Override
        public void onStateChanged(AppState oldAppState, AppState newAppState) {
            rebindState(newAppState);
        }
    };

}
