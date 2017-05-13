package erika.app.coffee.model;

import erika.app.coffee.service.Client;
import erika.app.coffee.service.communication.response.Response;

public interface ResponseHandler {

    boolean handle(Response response);

    void statusChanged(Client.Status status, Client.Status tryingStatus);
}
