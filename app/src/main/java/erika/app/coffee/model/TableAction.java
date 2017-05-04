package erika.app.coffee.model;

import android.support.annotation.IntDef;

@IntDef({
        TableAction.CHECK_OUT,
        TableAction.CHECK_OUT_AND_PRINT,
        TableAction.CANCEL_SERVICE,
})
public @interface TableAction {
    int CHECK_OUT = 1;
    int CHECK_OUT_AND_PRINT = 2;
    int CANCEL_SERVICE = 0;
}
