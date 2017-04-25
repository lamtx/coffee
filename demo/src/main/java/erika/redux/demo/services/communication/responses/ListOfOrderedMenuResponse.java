package erika.redux.demo.services.communication.responses;

import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;
import erika.redux.demo.services.communication.OrderedMenuItem;

public class ListOfOrderedMenuResponse extends Response {
    public final int tableId;
    public final  String tableName;
    public final  OrderedMenuItem[] menus;
    public final  String total;

    private ListOfOrderedMenuResponse(Reader reader) throws MissingFieldException {
        tableId = reader.readInt();
        tableName = reader.readString();
        total = reader.readString();
        menus = reader.readArrayObject(OrderedMenuItem.READER);
    }

    static final Parser<ListOfOrderedMenuResponse> PARSER = ListOfOrderedMenuResponse::new;
}