package erika.core.threading;

import android.os.AsyncTask;

import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class TaskFactory {

    public static <V> Task<V> startNew(final TaskBody<V> statement) {
        final TaskCompletionSource<V> source = new TaskCompletionSource<>();
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected final Void doInBackground(Void... params) {
                V value;
                try {
                    value = statement.apply();
                } catch (CancellationException e) {
                    source.setCancellation();
                    return null;
                } catch (Exception e) {
                    source.setException(e);
                    return null;
                }
                source.setResult(value);
                return null;
            }

        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return source.getTask();
    }

    public static <V> Task<V> startFromFuture(final Future<V> task, final long milliseconds) {
        return TaskFactory.startNew(new TaskBody<V>() {
            @Override
            public V apply() throws Exception {
                try {
                    return task.get(milliseconds, TimeUnit.MILLISECONDS);
                } catch (TimeoutException ignored) {
                    throw new CancellationException();
                }
            }
        });
    }
}
