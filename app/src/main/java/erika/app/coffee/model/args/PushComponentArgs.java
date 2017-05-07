package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.BackStackElement;

public class PushComponentArgs extends Args {
    public final BackStackElement element;

    public PushComponentArgs(BackStackElement element) {
        super(ActionType.PUSH);
        this.element = element;
    }
}
