package erika.app.coffee.service.communication.responses;

import erika.app.coffee.service.communication.OrderedMenuItem;
import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;

public class ListOfOrderedMenuResponse extends Response {
    public final int tableId;
    public final  String tableName;
    public final  OrderedMenuItem[] menus;

    private ListOfOrderedMenuResponse(Reader reader) throws MissingFieldException {
        tableId = reader.readInt();
        tableName = reader.readString();
        menus = reader.readArrayObject(OrderedMenuItem.READER);
    }

    static final Parser<ListOfOrderedMenuResponse> PARSER = ListOfOrderedMenuResponse::new;
}