package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.model.Message;

public class AddMessageArgs extends Args {
    public final Message message;

    public AddMessageArgs(String message, int messageId, LoadState status) {
        super(ActionType.ADD_MESSAGE);
        this.message = new Message(message, messageId, status);
    }
}
