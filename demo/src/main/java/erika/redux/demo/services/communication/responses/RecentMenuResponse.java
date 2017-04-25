package erika.redux.demo.services.communication.responses;

import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;
import erika.redux.demo.services.communication.MenuCategory;

public class RecentMenuResponse extends Response {
    public final MenuCategory menu;

    private RecentMenuResponse(Reader reader) throws MissingFieldException {
        menu = reader.readObject(MenuCategory.READER);
    }

    static final Parser<RecentMenuResponse> PARSER = RecentMenuResponse::new;
}