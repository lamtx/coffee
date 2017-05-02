package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;

public class SetLoadingMessageArgs extends Args {
    public String message;

    public SetLoadingMessageArgs(String message) {
        super(ActionType.SET_LOADING_DIALOG_MESSAGE);
        this.message = message;
    }
}
