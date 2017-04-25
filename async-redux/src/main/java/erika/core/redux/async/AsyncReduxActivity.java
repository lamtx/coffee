package erika.core.redux.async;

import erika.core.redux.ReduxActivity;
import erika.core.redux.Store;

public abstract class AsyncReduxActivity<AppState, State> extends ReduxActivity<AppState, State> {
    @Override
    public Store<AppState> getStore() {
        return super.getStore();
    }
}
