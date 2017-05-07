package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;

public class RemoveMessageArgs extends Args {
    public final int messageId;

    public RemoveMessageArgs(int messageId) {
        super(ActionType.REMOVE_MESSAGE);
        this.messageId = messageId;
    }
}
