package erika.redux.demo.models;

import erika.redux.demo.services.communication.responses.Response;

public interface ResponseHandler {

    boolean handle(Response response);

    void statusChanged(int status, String message);
}
