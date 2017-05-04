package erika.app.coffee.service.communication.responses;

import java.util.List;

import erika.app.coffee.service.communication.OrderedMenuItem;
import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;

public class ListOfOrderedMenuResponse extends Response {
    public final int tableId;
    public final String tableName;
    public final List<OrderedMenuItem> menus;

    public ListOfOrderedMenuResponse(int tableId, String tableName, List<OrderedMenuItem> menus) {
        this.tableId = tableId;
        this.tableName = tableName;
        this.menus = menus;
    }

    private ListOfOrderedMenuResponse(Reader reader) throws MissingFieldException {
        tableId = reader.readInt();
        tableName = reader.readString();
        menus = reader.readArrayListObject(OrderedMenuItem.READER);
    }

    static final Parser<ListOfOrderedMenuResponse> PARSER = ListOfOrderedMenuResponse::new;
}