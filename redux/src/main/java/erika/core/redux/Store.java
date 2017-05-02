package erika.core.redux;


import erika.core.redux.utils.LinkedList;
import erika.core.redux.utils.LinkedListNode;

public class Store<State> implements Dispatcher {
    interface OnStateChangedListener<State> {
        void onStateChanged(State oldState, State newState);
    }

    private final Reducer<State> reducer;
    private State state;

    private final LinkedList<OnStateChangedListener<State>> listeners = new LinkedList<>();

    public Store(Reducer<State> reducer) {
        this.reducer = reducer;
    }

    @Override
    public void dispatch(Action action) {
        State oldState = this.state;
        this.state = reducer.reduce(this.state, action);
        if (this.state != oldState) {
            notifyStateChanged(oldState, state);
        }
    }

    @Override
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

    void registerStateChangedListener(OnStateChangedListener<State> listener) {
        listeners.add(listener);
    }

    void unregisterStateChangedListener(OnStateChangedListener<State> listener) {
        for (LinkedListNode<OnStateChangedListener<State>> node = listeners.getHead(); node != null; node = node.getNext()) {
            OnStateChangedListener<State> value = node.getValue();
            if (value == listener) {
                listeners.remove(node);
            }
        }
    }
}
