package erika.redux.demo.models;

import erika.redux.demo.services.communication.responses.Response;

public abstract class ResponseAction<T extends Response> {

    public abstract void receive(T response);

    public void cancel() {

    }
}
