package erika.app.coffee.application;

import android.text.format.DateUtils;

import java.text.DecimalFormat;

public class Define {
    private Define() {
    }

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###,###,###");
    public static final int PORT = 36524;
    public static final int CONNECTION_TIMEOUT = (int)(200 * DateUtils.SECOND_IN_MILLIS);
}
