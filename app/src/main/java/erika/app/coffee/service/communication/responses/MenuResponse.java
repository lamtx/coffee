package erika.app.coffee.service.communication.responses;

import java.util.ArrayList;

import erika.app.coffee.service.communication.MenuCategory;
import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;

public class MenuResponse extends Response {
    public final ArrayList<MenuCategory> menu;

    protected MenuResponse(Reader reader) throws MissingFieldException {
        menu = reader.readArrayListObject(MenuCategory.READER);
    }

    static final Parser<MenuResponse> PARSER = MenuResponse::new;
}
