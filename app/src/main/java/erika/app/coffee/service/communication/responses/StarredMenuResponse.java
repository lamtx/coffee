package erika.app.coffee.service.communication.responses;


import erika.app.coffee.service.communication.MenuCategory;
import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;

public class StarredMenuResponse extends Response {
    public final MenuCategory menu;

    private StarredMenuResponse(Reader reader) throws MissingFieldException {
        menu = reader.readObject(MenuCategory.READER);
    }

    static final Parser<StarredMenuResponse> PARSER = StarredMenuResponse::new;
}
