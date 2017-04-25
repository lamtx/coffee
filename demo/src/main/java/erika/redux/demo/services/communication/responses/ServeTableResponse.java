package erika.redux.demo.services.communication.responses;

import android.text.TextUtils;

import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;

public class ServeTableResponse extends Response {
    public final String message;

    public boolean isSuccessful() {
        return TextUtils.isEmpty(message);
    }

    private ServeTableResponse(Reader reader) throws MissingFieldException {
        message = reader.readString();
    }

    static final Parser<ServeTableResponse> PARSER = ServeTableResponse::new;
}