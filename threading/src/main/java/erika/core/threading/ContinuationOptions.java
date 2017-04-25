package erika.core.threading;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef(value = { ContinuationOptions.NONE,
        ContinuationOptions.NOT_ON_CANCELLED,
        ContinuationOptions.NOT_ON_FAULTED,
        ContinuationOptions.ONLY_COMPLETED,
        ContinuationOptions.EXECUTE_SYNCHRONOUSLY}, flag = true)
@Retention(RetentionPolicy.SOURCE)
public @interface ContinuationOptions {
    int NONE = 0;
    int NOT_ON_CANCELLED = 1 << 1;
    int NOT_ON_FAULTED = 1 << 2;
    int EXECUTE_SYNCHRONOUSLY = 1 << 3;
    int ONLY_COMPLETED = NOT_ON_FAULTED | NOT_ON_CANCELLED;
}
