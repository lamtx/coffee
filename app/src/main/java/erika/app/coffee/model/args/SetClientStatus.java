package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.service.Client;

public class SetClientStatus extends Args {
    public final Client.Status status;
    public final String message;

    public SetClientStatus(Client.Status status, String message) {
        super(ActionType.SET_CLIENT_STATUS);
        this.status = status;
        this.message = message;
    }
}
