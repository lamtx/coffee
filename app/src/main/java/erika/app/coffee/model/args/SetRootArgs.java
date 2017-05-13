package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.BackStackElement;

public class SetRootArgs extends Args {
    public final BackStackElement element;

    public SetRootArgs(BackStackElement element) {
        super(ActionType.SET_ROOT);
        this.element = element;
    }
}
