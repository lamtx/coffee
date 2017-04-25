package erika.redux.demo.services.communication.responses;

import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;

public class ViewBillResponse extends Response {
    public String text;

    private ViewBillResponse(Reader reader) throws MissingFieldException {
        text = reader.readString();
    }
    static final Parser<ViewBillResponse> PARSER = ViewBillResponse::new;
}
