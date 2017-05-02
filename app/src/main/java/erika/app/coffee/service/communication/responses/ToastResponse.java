package erika.app.coffee.service.communication.responses;

import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;

public class ToastResponse extends Response {
    public final String message;

    private ToastResponse(Reader reader) throws MissingFieldException {
        message = reader.readString();
    }

    static final Parser<ToastResponse> PARSER = ToastResponse::new;
}