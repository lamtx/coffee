package erika.app.coffee.service.communication.response;

import android.text.TextUtils;

import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;

public class ServeTableResponse extends Response {
    public final String message;

    public boolean isSuccessful() {
        return TextUtils.isEmpty(message);
    }

    public ServeTableResponse(Reader reader) throws MissingFieldException {
        message = reader.readString();
    }

    public ServeTableResponse(String message) {
        this.message = message;
    }

    static final Parser<ServeTableResponse> PARSER = ServeTableResponse::new;
}