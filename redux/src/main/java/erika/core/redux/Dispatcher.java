package erika.core.redux;

public interface Dispatcher {
    void dispatch(Action action);

    void dispatch(DispatchAction action);
}
