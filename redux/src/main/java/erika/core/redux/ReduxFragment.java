package erika.core.redux;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.annotation.CallSuper;

import erika.core.redux.utils.StateBinder;

public abstract class ReduxFragment<AppState, State> extends Fragment implements Component<AppState, State> {
    private final StateBinder<AppState> binder = new StateBinder<>();
    private State state;
    private boolean isAlive;

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    public Store<AppState> getStore() {
        ReduxApplication<AppState> application = (ReduxApplication<AppState>) getActivity().getApplication();
        return application.getStore();
    }

    @Override
    @CallSuper
    public void bindStateToView(State state) {
        binder.rebindState(getStore().getState());
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof Component) {
            Component<AppState, ?> component = (Component<AppState, ?>) childFragment;
            binder.onComponentAdded(component);
        }
    }

    public void onDetachFragment(Fragment childFragment) {
        if (childFragment instanceof Component) {
            binder.onComponentRemoved(((Component) childFragment));
        }
    }

    @Override
    public abstract State getStateFromStore(AppState state);

    @Override
    public void onStart() {
        super.onStart();
        isAlive = true;
        bindStateToView(getState());
    }

    @Override
    public void onStop() {
        super.onStop();
        isAlive = false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ReduxApplication<AppState> app = (ReduxApplication<AppState>) activity.getApplicationContext();
        AppState appState = app.getStore().getState();
        setState(getStateFromStore(appState));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            if (parentFragment instanceof ReduxFragment) {
                ((ReduxFragment) parentFragment).onDetachFragment(this);
            }
        } else {
            if (getActivity() instanceof ReduxActivity) {
                ReduxActivity activity = (ReduxActivity) getActivity();
                activity.onDetachFragment(this);
            }
        }
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

    public void dispatch(Action action) {
        getStore().dispatch(action);
    }

    public void dispatch(DispatchAction action) {
        getStore().dispatch(action);
    }
}
