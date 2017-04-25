package erika.core.threading;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public abstract class TaskScheduler {

    private static TaskScheduler s_synchronized = null;
    private static TaskScheduler s_background = null;
    /**
     * For debugging
     */
    private final String name;


    TaskScheduler(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * The {@code TaskScheduler} that performs task in main thread
     *
     * @return TaskScheduler
     */
    public synchronized static TaskScheduler synchronizedContext() {
        if (s_synchronized == null) {
            s_synchronized = new HandlerTaskScheduler("Synchronized TaskScheduler", new Handler(Looper.getMainLooper()));
        }
        return s_synchronized;
    }

    /**
     * The {@code TaskScheduler} that performs task in main thread pool
     *
     * @return TaskScheduler
     */
    public static synchronized TaskScheduler backgroundContext() {
        if (s_background == null) {
            s_background = new ThreadPoolTaskScheduler("Background TaskScheduler");
        }
        return s_background;
    }

    public static TaskScheduler fromHandler(Handler handler) {
        return new HandlerTaskScheduler("Unknown Handler TaskScheduler", handler);
    }

    public abstract void post(Runnable action);
}

class HandlerTaskScheduler extends TaskScheduler {
    private final Handler handler;

    HandlerTaskScheduler(String name, Handler handler) {
        super(name);
        this.handler = handler;
    }

    public void post(Runnable action) {
        handler.post(action);
    }
}

class ThreadPoolTaskScheduler extends TaskScheduler {
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(6);

    ThreadPoolTaskScheduler(String name) {
        super(name);
    }

    @Override
    public void post(final Runnable action) {
        executor.execute(action);
    }
}
