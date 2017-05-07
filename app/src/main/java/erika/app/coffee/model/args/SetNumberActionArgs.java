package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.state.NumberState;

public class SetNumberActionArgs extends Args {
    public final NumberState.Action action;

    public SetNumberActionArgs(NumberState.Action action) {
        super(ActionType.SET_NUMBER_ACTION);
        this.action = action;
    }
}
