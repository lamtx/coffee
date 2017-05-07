package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.Message;

public class ChangeMessageStatusArgs extends Args {
    public final int messageId;
    public final Message.Status status;

    public ChangeMessageStatusArgs(int messageId, Message.Status status) {
        super(ActionType.CHANGE_MESSAGE_STATUS);
        this.messageId = messageId;
        this.status = status;
    }
}
