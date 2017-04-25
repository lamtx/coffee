package erika.redux.demo.services.communication.responses;

import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;

public class NumberItemOfMenuResponse extends Response {
    public final String description;

    private NumberItemOfMenuResponse(Reader reader) throws MissingFieldException {
        description = reader.readString();
    }

    static final Parser<NumberItemOfMenuResponse> PARSER = NumberItemOfMenuResponse::new;
}
