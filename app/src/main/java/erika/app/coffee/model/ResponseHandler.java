package erika.app.coffee.model;

import erika.app.coffee.service.Client;
import erika.app.coffee.service.communication.responses.Response;

public interface ResponseHandler {

    boolean handle(Response response);

    void statusChanged(Client.ConnectionStatus status, Client.ConnectionStatus tryingStatus);
}
