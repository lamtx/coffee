package erika.app.coffee.service.communication.responses;

import java.util.HashMap;
import java.util.Map;

import erika.core.communication.Parser;
import erika.core.communication.ReadableMessage;

public abstract class Response extends ReadableMessage {
    private static final Map<String, Parser<? extends Response>> PARSERS = new HashMap<>();

    private static void createParsers() {
        PARSERS.put(ListOfOrderedMenuResponse.class.getSimpleName(), ListOfOrderedMenuResponse.PARSER);
        PARSERS.put(ListOfTableResponse.class.getSimpleName(), ListOfTableResponse.PARSER);
        PARSERS.put(MenuResponse.class.getSimpleName(), MenuResponse.PARSER);
        PARSERS.put(ServeTableResponse.class.getSimpleName(), ServeTableResponse.PARSER);
        PARSERS.put(SuccessResponse.class.getSimpleName(), SuccessResponse.PARSER);
        PARSERS.put(ToastResponse.class.getSimpleName(), ToastResponse.PARSER);
    }

    public static final Parser<Response> PARSER = reader -> {
        if (PARSERS.isEmpty()) {
            createParsers();
        }
        String name = reader.readString();
        int sequenceId = reader.readInt();
        Parser<? extends Response> parser = PARSERS.get(name);
        if (parser != null) {
            Response response = parser.parse(reader);
            response.setSequenceId(sequenceId);
            return response;
        }
        return null;
    };
}
