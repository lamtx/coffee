package erika.app.coffee.service.communication.response;

import java.util.List;

import erika.app.coffee.service.communication.MenuCategory;
import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;

public class MenuResponse extends Response {
    public final List<MenuCategory> categories;

    public MenuResponse(List<MenuCategory> categories) {
        this.categories = categories;
    }

    protected MenuResponse(Reader reader) throws MissingFieldException {
        categories = reader.readArrayListObject(MenuCategory.READER);
    }

    static final Parser<MenuResponse> PARSER = MenuResponse::new;
}
