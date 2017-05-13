package erika.core.redux;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class ReduxActivity<AppState, State> extends AppCompatActivity implements Component<AppState, State> {
    private State state;
    private Store<AppState> store;
    private boolean bound = false;
    @Override
    public State getState() {
        return state;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        @SuppressWarnings({"unchecked"})
        ReduxApplication<AppState> application = (ReduxApplication<AppState>) getApplication();
        store = application.getStore();
        state = getStateFromStore(store.getState());
    }

    @Override
    protected void onStart() {
        super.onStart();
        store.registerStateChangedListener(onStateChangedListener);
        rebindState(store.getState());
    }

    @Override
    protected void onStop() {
        store.unregisterStateChangedListener(onStateChangedListener);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        store = null;
        super.onDestroy();
    }

    private void rebindState(AppState newAppState) {
        State oldState = getState();
        State newState = getStateFromStore(newAppState);
        if (!bound || newState != oldState) {
            bound = true;
            willReceiveState(newState);
            state = newState;
            bindStateToView(newState);
        }
    }

    public void dispatch(Action action) {
        store.dispatch(action);
    }

    public void dispatch(DispatchAction action) {
        store.dispatch(action);
    }

    private final Store.OnStateChangedListener<AppState> onStateChangedListener = new Store.OnStateChangedListener<AppState>() {
        @Override
        public void onStateChanged(AppState oldAppState, AppState newAppState) {
            rebindState(newAppState);
        }
    };

}
