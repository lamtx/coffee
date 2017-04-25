package erika.core.redux;

public interface Component<AppState, State> {

    State getState();

    void setState(State state);

    void bindStateToView(State state);

    State getStateFromStore(AppState state);

    boolean isAlive();
}
