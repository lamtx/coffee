package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;

public class SetNumberValueArgs extends Args {
    public final String value;

    public SetNumberValueArgs(String value) {
        super(ActionType.SET_NUMBER_VALUE);
        this.value = value;
    }
}
