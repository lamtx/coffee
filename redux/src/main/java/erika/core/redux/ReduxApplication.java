package erika.core.redux;

import android.app.Application;
import android.support.annotation.NonNull;

public abstract class ReduxApplication<AppState> extends Application {

    private Store<AppState> store;

    @NonNull
    public abstract Store<AppState> createStore();

    @Override
    public void onCreate() {
        super.onCreate();
        store = createStore();
        dispatch(EMPTY_ACTION);
    }

    public Store<AppState> getStore() {
        return store;
    }

    public void dispatch(Action action) {
        store.dispatch(action);
    }

    public void dispatch(DispatchAction action) {
        store.dispatch(action);
    }

    public static final Action EMPTY_ACTION = new Action() {

        @Override
        public String getType() {
            return "";
        }
    };

}
