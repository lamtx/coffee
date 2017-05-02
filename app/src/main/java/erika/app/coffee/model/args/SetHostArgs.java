package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;

public class SetHostArgs extends Args {
    public final String host;

    public SetHostArgs(String host) {
        super(ActionType.SET_HOST);
        this.host = host;
    }
}
