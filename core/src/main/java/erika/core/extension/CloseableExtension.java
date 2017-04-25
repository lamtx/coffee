package erika.core.extension;

import java.io.Closeable;
import java.io.IOException;

public class CloseableExtension {

    private CloseableExtension() {
    }

    public static void forceClose(Closeable target) {
        if (target != null) {
            try {
                target.close();
            } catch (IOException ignored) {
            }
        }
    }
}
