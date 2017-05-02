package erika.app.coffee.model;

import erika.app.coffee.service.communication.responses.Response;

public abstract class ResponseAction<T extends Response> {

    public abstract void receive(T response);

    public void cancel() {

    }
}
