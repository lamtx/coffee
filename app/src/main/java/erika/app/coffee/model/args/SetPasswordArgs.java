package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;

public class SetPasswordArgs extends Args {
    public final String password;

    public SetPasswordArgs(String password) {
        super(ActionType.SET_PASSWORD);
        this.password = password;
    }
}
