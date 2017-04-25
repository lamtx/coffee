package erika.core.redux.async;

import erika.core.redux.Action;
import erika.core.redux.DispatchAction;
import erika.core.redux.Reducer;
import erika.core.redux.Store;
import erika.core.threading.Task;
import erika.core.threading.TaskCompletionSource;

public class AsyncStore<State> extends Store<State> {
    public AsyncStore(Reducer<State> reducer) {
        super(reducer);
    }

    public Task<Void> dispatchAsync(Action action) {
        TaskCompletionSource<Void> source = new TaskCompletionSource<>();
        super.dispatch(action);
        return source.getTask();
    }

    public Task<Void> dispatchAsync(DispatchAction action) {
        TaskCompletionSource<Void> source = new TaskCompletionSource<>();
        super.dispatch(action);
        return source.getTask();
    }
}
