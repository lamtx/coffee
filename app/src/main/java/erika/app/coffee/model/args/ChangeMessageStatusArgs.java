package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.LoadState;

public class ChangeMessageStatusArgs extends Args {
    public final int messageId;
    public final LoadState status;

    public ChangeMessageStatusArgs(int messageId,LoadState status) {
        super(ActionType.CHANGE_MESSAGE_STATUS);
        this.messageId = messageId;
        this.status = status;
    }
}
