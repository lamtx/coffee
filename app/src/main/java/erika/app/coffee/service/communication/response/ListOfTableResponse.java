package erika.app.coffee.service.communication.response;

import java.util.List;

import erika.app.coffee.service.communication.Table;
import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;

public class ListOfTableResponse extends Response {
    public final List<Table> tables;

    private ListOfTableResponse(Reader reader) throws MissingFieldException {
        tables = reader.readArrayListObject(Table.READER);
    }

    public ListOfTableResponse(List<Table> tables) {
        this.tables = tables;
    }

    static final Parser<ListOfTableResponse> PARSER = ListOfTableResponse::new;
}
