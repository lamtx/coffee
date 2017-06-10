package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;

public class SetNumberStateArgs extends Args {
    public final boolean decreaseMode;
    public final String title;

    public SetNumberStateArgs(String title, boolean decreaseMode) {
        super(ActionType.SET_NUMBER_STATE);
        this.decreaseMode = decreaseMode;
        this.title = title;
    }

}
