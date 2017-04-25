package erika.core.threading;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureSource<V> {
    private final RequestFuture<V> future = new RequestFuture<>();

    public Future<V> getFuture() {
       return future;
    }

    public void setResult(V result) {
        future.setResult(result);
    }

    private static class RequestFuture<T> implements Future<T>{
        private boolean mResultReceived = false;
        private T mResult;
        private Throwable mException;


        @Override
        public synchronized boolean cancel(boolean mayInterruptIfRunning) {
           throw new UnsupportedOperationException();
        }

        @Override
        public T get() throws InterruptedException, ExecutionException {
            try {
                return doGet(null);
            } catch (TimeoutException e) {
                throw new AssertionError(e);
            }
        }

        @Override
        public T get(long timeout, @NonNull TimeUnit unit)
                throws InterruptedException, ExecutionException, TimeoutException {
            return doGet(TimeUnit.MILLISECONDS.convert(timeout, unit));
        }

        private synchronized T doGet(Long timeoutMs)
                throws InterruptedException, ExecutionException, TimeoutException {
            if (mException != null) {
                throw new ExecutionException(mException);
            }

            if (mResultReceived) {
                return mResult;
            }

            if (timeoutMs == null) {
                wait(0);
            } else if (timeoutMs > 0) {
                wait(timeoutMs);
            }

            if (mException != null) {
                throw new ExecutionException(mException);
            }

            if (!mResultReceived) {
                throw new TimeoutException();
            }

            return mResult;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public synchronized boolean isDone() {
            return mResultReceived || mException != null || isCancelled();
        }

        public synchronized void setResult(T result) {
            mResultReceived = true;
            mResult = result;
            notifyAll();
        }

        public synchronized void setException(Throwable error) {
            mException = error;
            notifyAll();
        }
    }


}
