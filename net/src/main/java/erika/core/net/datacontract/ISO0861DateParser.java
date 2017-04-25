package erika.core.net.datacontract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ISO0861DateParser {
    private static final SimpleDateFormat ISO08601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
    private static final SimpleDateFormat NO_TIME_ZONE = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    public static Date parse(String input) throws ParseException {
        if (input == null || input.length() == 0) {
            return null;
        }
        return NO_TIME_ZONE.parse(input);
    }

    public static Date tryParse(String input) {
        try {
            return parse(input);
        } catch (ParseException ignored) {
            return null;
        }
    }

    public static String format(Date date) {
        if (date == null) {
            return null;
        }
        return NO_TIME_ZONE.format(date);
    }

}
