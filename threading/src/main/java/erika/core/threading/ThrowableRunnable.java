package erika.core.threading;

/**
 * Like {@code java.lang.Runnable} but is able to throw exception.
 *
 * @author Starfall
 */
public interface ThrowableRunnable {
    void run() throws Exception;

    class ToRunnable {
        public static Runnable toRunnable(final ThrowableRunnable r) {
            return new Runnable() {

                @Override
                public void run() {
                    try {
                        r.run();
                    } catch (Exception e) {
                        throw new RuntimeException("Unable wrap ThrowableRunnable to Runnable", e);
                    }
                }
            };
        }
    }
}
