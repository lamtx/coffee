package erika.redux.demo.services.communication.responses;

import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;
import erika.redux.demo.services.communication.Table;

public class ListOfTableResponse extends Response {
    public final Table[] tables;

    private ListOfTableResponse(Reader reader) throws MissingFieldException {
        tables = reader.readArrayObject(Table.READER);
    }

    static final Parser<ListOfTableResponse> PARSER = ListOfTableResponse::new;
}
