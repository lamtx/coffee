package erika.core.redux;

import android.os.Handler;
import android.support.annotation.MainThread;

import erika.core.redux.utils.LinkedList;
import erika.core.redux.utils.LinkedListNode;

public class Store<State> implements Dispatcher {
    interface OnStateChangedListener<State> {
        void onStateChanged(State oldState, State newState);
    }


    private final Reducer<State> reducer;
    private State state;
    private State oldState;
    private final Handler handler = new Handler();

    private final LinkedList<OnStateChangedListener<State>> listeners = new LinkedList<>();

    public Store(Reducer<State> reducer) {
        this.reducer = reducer;
    }

    @Override
    @MainThread
    public void dispatch(Action action) {
        oldState = state;
        state = reducer.reduce(state, action);
        if (state != oldState) {
            handler.removeCallbacks(notifyStateAction);
            handler.post(notifyStateAction);
        }
    }

    @Override
    @MainThread
    public void dispatch(DispatchAction action) {
        action.onDispatch(this);
    }

    private void notifyStateChanged(State oldState, State newState) {
        for (OnStateChangedListener<State> listener : listeners) {
            listener.onStateChanged(oldState, newState);
        }
    }

    public State getState() {
        return state;
    }

    @MainThread
    public void registerStateChangedListener(OnStateChangedListener<State> listener) {
        listeners.add(listener);
    }

    @MainThread
    public void unregisterStateChangedListener(OnStateChangedListener<State> listener) {
        for (LinkedListNode<OnStateChangedListener<State>> node = listeners.getHead(); node != null; node = node.getNext()) {
            OnStateChangedListener<State> value = node.getValue();
            if (value == listener) {
                listeners.remove(node);
            }
        }
    }

    private final Runnable notifyStateAction = new Runnable() {
        @Override
        public void run() {
            notifyStateChanged(oldState, state);
            oldState = null;
        }
    };
}
