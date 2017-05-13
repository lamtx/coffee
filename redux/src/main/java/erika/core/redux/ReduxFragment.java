package erika.core.redux;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.CallSuper;

public abstract class ReduxFragment<AppState, State> extends Fragment implements Component<AppState, State> {
    private State state;
    private Store<AppState> store;
    private boolean bound = false;
    @Override
    public State getState() {
        return state;
    }

    private void setState(State state) {
        this.state = state;
    }

    public Store<AppState> getStore() {
        return store;
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        @SuppressWarnings({"unchecked"})
        ReduxApplication<AppState> reduxActivity = (ReduxApplication) activity.getApplication();
        store = reduxActivity.getStore();
        state = getStateFromStore(store.getState());
        bound = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        getStore().registerStateChangedListener(onStateChangedListener);
        rebindState(getStore().getState());
    }

    @Override
    public void onStop() {
        getStore().unregisterStateChangedListener(onStateChangedListener);
        super.onStop();
    }

    @Override
    public void onDetach() {
        store = null;
        state = null;
        super.onDetach();
    }

    private void rebindState(AppState newAppState) {
        State oldState = getState();
        State newState = getStateFromStore(newAppState);
        if (!bound || newState != oldState) {
            bound = true;
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
