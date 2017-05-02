package erika.app.coffee.utility;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import erika.app.coffee.service.communication.Table;

public class Utils {
    private static final DecimalFormat DECIMAL_FORMAT;
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DECIMAL_FORMAT = new DecimalFormat("###,###,###,###", symbols);
    }

    private Utils() {
    }

    public static Table[] remove(Table[] source, Table exclusive) {
        if (source.length > 0) {
            Table[] result = new Table[source.length - 1];
            int j = 0;
            for (Table e : source) {
                if (e.id != exclusive.id) {
                    result[j++] = e;
                }
            }
            return result;
        } else {
            return source;
        }
    }

    public static String stringFrom(double value) {
        return DECIMAL_FORMAT.format(value);
    }

    public static String stringFrom(int value) {
        return DECIMAL_FORMAT.format(value);
    }
}
