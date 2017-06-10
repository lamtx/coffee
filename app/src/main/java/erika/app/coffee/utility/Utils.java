package erika.app.coffee.utility;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import erika.app.coffee.service.communication.OrderedMenuItem;
import erika.app.coffee.service.communication.Table;
import erika.core.ArrayHelper;
import erika.core.Arrays;

public class Utils {
    private static final DecimalFormat DECIMAL_FORMAT;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
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

    public static String stringFrom(long value) {
        return DECIMAL_FORMAT.format(value);
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            IBinder token = view.getWindowToken();
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(token, 0);
        }
    }

    public static double calcTotal(List<OrderedMenuItem> items) {
        return Arrays.sum(items, (ArrayHelper.DoubleCreator<OrderedMenuItem>) x -> x.quantity * (double)x.menuItem.price);
    }
}
