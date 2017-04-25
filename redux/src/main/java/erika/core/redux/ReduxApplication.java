package erika.core.redux;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;

import erika.core.redux.utils.StateBinder;

public abstract class ReduxApplication<AppState> extends Application {

    private Store<AppState> store;
    private final StateBinder<AppState> binder = new StateBinder<>();

    @NonNull
    public abstract Store<AppState> createStore();

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new LifecycleCallbacks());
        store = createStore();
        store.dispatch(new EmptyAction());
        store.registerStateChangedListener(new StateChangedListener());
    }

    public Store<AppState> getStore() {
        return store;
    }

    private class LifecycleCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (activity instanceof Component) {
                binder.onComponentAdded(((Component<AppState, ?>) activity));
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (activity instanceof Component) {
                binder.onComponentRemoved((Component) activity);
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

    private class StateChangedListener implements Store.OnStateChangedListener<AppState> {

        @Override
        public void onStateChanged(AppState oldState, AppState newState) {
            binder.rebindState(newState);
        }
    }

    private static class EmptyAction implements Action {

        @Override
        public String getType() {
            return "";
        }
    }
}
