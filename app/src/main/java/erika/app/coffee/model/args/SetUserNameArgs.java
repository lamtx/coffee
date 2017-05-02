package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;

public class SetUserNameArgs extends Args {
    public final String userName;

    public SetUserNameArgs(String userName) {
        super(ActionType.SET_USER_NAME);
        this.userName = userName;
    }
}
