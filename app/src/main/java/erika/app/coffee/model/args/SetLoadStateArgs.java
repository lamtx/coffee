package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.LoadState;

public class SetLoadStateArgs extends Args {
    public final Class<?> callingAction;
    public final LoadState loadState;

    public SetLoadStateArgs(Class<?> callingAction, LoadState loadState) {
        super(ActionType.SET_LOAD_STATE);
        this.callingAction = callingAction;
        this.loadState = loadState;
    }
}
