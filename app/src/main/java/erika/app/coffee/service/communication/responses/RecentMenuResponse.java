package erika.app.coffee.service.communication.responses;

import erika.app.coffee.service.communication.MenuCategory;
import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;

public class RecentMenuResponse extends Response {
    public final MenuCategory menu;

    private RecentMenuResponse(Reader reader) throws MissingFieldException {
        menu = reader.readObject(MenuCategory.READER);
    }

    static final Parser<RecentMenuResponse> PARSER = RecentMenuResponse::new;
}