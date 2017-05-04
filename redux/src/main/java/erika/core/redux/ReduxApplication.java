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
        store.dispatch(new EmptyAction());
    }

    public Store<AppState> getStore() {
        return store;
    }

    private static class EmptyAction implements Action {

        @Override
        public String getType() {
            return "";
        }
    }
}
