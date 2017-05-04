package erika.core.redux;

public interface Component<AppState, State> {

    State getState();

    void bindStateToView(State state);

    State getStateFromStore(AppState state);

    void willReceiveState(State state);
}
