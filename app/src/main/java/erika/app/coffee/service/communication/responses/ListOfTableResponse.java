package erika.app.coffee.service.communication.responses;

import android.support.annotation.VisibleForTesting;

import erika.app.coffee.service.communication.Table;
import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;

public class ListOfTableResponse extends Response {
    public final Table[] tables;

    private ListOfTableResponse(Reader reader) throws MissingFieldException {
        tables = reader.readArrayObject(Table.READER);
    }

    public ListOfTableResponse(Table[] tables) {
        this.tables = tables;
    }

    static final Parser<ListOfTableResponse> PARSER = ListOfTableResponse::new;
}
