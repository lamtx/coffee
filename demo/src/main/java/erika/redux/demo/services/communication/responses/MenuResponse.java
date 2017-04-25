package erika.redux.demo.services.communication.responses;

import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;
import erika.redux.demo.services.communication.MenuCategory;

public class MenuResponse extends Response {
    private MenuCategory[] menu;

    protected MenuResponse(Reader reader) throws MissingFieldException {
        menu = reader.readArrayObject(MenuCategory.READER);
    }

    static final Parser<MenuResponse> PARSER = MenuResponse::new;
}
