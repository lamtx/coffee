package erika.app.coffee.service.communication.response;

import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;

public class SuccessResponse extends Response {
    public final boolean successful;
    public final String toast;

    public SuccessResponse(boolean successful, String toast) {
        this.successful = successful;
        this.toast = toast;
    }

    private SuccessResponse(Reader reader) throws MissingFieldException {
        successful = reader.readBoolean();
        toast = reader.readString();
    }

    static final Parser<SuccessResponse> PARSER = SuccessResponse::new;
}
