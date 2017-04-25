package erika.core.threading;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.CancellationException;

public final class Task<V> {

    private final TaskData<V> data;

    Task(TaskData<V> data) {
        this.data = data;
    }

    public boolean isFaulted() {
        return getStatus() == TaskStatus.Faulted;
    }

    public boolean isCancelled() {
        return getStatus() == TaskStatus.Cancelled;
    }

    public boolean isCompleted() {
        return getStatus() == TaskStatus.Completed;
    }

    public TaskStatus getStatus() {
        return data.status;
    }

    public Exception getException() {
        return data.exception;
    }

    public V getResult() {
        if (isCancelled()) {
            throw new TaskIsFaultedException("Get result of task while task is cancelled", new CancellationException());
        }
        if (isFaulted()) {
            throw new TaskIsFaultedException("Get result of task while task is faulted", getException());
        }
        return data.result;
    }

    public <NewResult> Task<NewResult> then(TaskContinuation<NewResult, V> continuation) {
        return then(continuation, ContinuationOptions.NONE);
    }

    public <NewResult> Task<NewResult> then(TaskContinuation<NewResult, V> continuation, @ContinuationOptions int continuationOptions) {
        return then(continuation, TaskScheduler.synchronizedContext(), continuationOptions);
    }

    public <NewResult> Task<NewResult> then(TaskContinuation<NewResult, V> continuation, TaskScheduler scheduler) {
        return then(continuation, scheduler, ContinuationOptions.NONE);
    }

    public Task<Void> then(TaskCompletion<V> continuation) {
        return then(continuation, ContinuationOptions.NONE);
    }

    public Task<Void> then(TaskCompletion<V> continuation, int createOption) {
        return then(continuation, TaskScheduler.synchronizedContext(), createOption);
    }

    public Task<Void> then(TaskCompletion<V> continuation, TaskScheduler scheduler) {
        return then(continuation, scheduler, ContinuationOptions.NONE);
    }

    public Task<Void> then(TaskCompletion<V> continuation, @NonNull TaskScheduler scheduler, @ContinuationOptions int continuationOptions) {
        return then(new TaskCompletionWrapper<V, Void>(continuation), scheduler, continuationOptions);
    }

    public <NewResult> Task<NewResult> then(TaskContinuation<NewResult, V> continuation, @NonNull TaskScheduler scheduler, @ContinuationOptions int continuationOptions) {
        if (data.continuationTask != null) {
            throw new IllegalStateException("This method must be called one time, or call {@code retain()} to remove last continuation action");
        }
        ContinueTask<NewResult, V> continueTask = new ContinueTask<>(continuation, data.currentScheduler);
        data.continuationTask = continueTask;
        data.taskContinuationOptions = continuationOptions;
        data.scheduler = scheduler;
        if (getStatus() != TaskStatus.Ready && getStatus() != TaskStatus.Running) {
            data.notifyCompletion(this);
        }
        return continueTask.getTask();
    }

    public <NewResult> Task<NewResult> join(TaskJoiner<NewResult, V> continuation) {
        return join(continuation, ContinuationOptions.NONE);
    }

    public <NewResult> Task<NewResult> join(TaskJoiner<NewResult, V> continuation, @ContinuationOptions int continuationOptions) {
        return join(continuation, TaskScheduler.synchronizedContext(), continuationOptions);
    }

    public <NewResult> Task<NewResult> join(TaskJoiner<NewResult, V> continuation, TaskScheduler scheduler) {
        return join(continuation, scheduler, ContinuationOptions.NONE);
    }

    public <NewResult> Task<NewResult> join(TaskJoiner<NewResult, V> continuation, @NonNull TaskScheduler scheduler, @ContinuationOptions int continuationOptions) {
        if (data.continuationTask != null) {
            throw new IllegalStateException("This method must be called one time, or call {@code retain()} to remove last continuation action");
        }
        JoinTask<NewResult, V> continueTask = new JoinTask<>(continuation, data.currentScheduler);
        data.continuationTask = continueTask;
        data.taskContinuationOptions = continuationOptions;
        data.scheduler = scheduler;
        if (getStatus() != TaskStatus.Ready && getStatus() != TaskStatus.Running) {
            data.notifyCompletion(this);
        }
        return continueTask.getTask();
    }

        /**
         * Remove continuation action of this task, allows call then() again
         *
         * @return Task has removed continuation action
         */
    public Task<V> retain() {
        return new Task<>(data.retain());
    }

    public void throwsIfAny() throws Exception {
        if (isCancelled()) {
            throw new CancellationException();
        }
        if (isFaulted()) {
            throw getException();
        }
    }

    /**
     * Get the {@code TaskScheduler} of calling thread.
     *
     * @return Return current scheduler if called in task, return TaskScheduler of current looper of main thread. if not, return default scheduler
     */
    @NonNull
    private TaskScheduler getCurrentContext() {
        if (data.currentScheduler != null) {
            return data.currentScheduler;
        }
        Looper myLooper = Looper.myLooper();
        if (myLooper != null) {
            if (myLooper == Looper.getMainLooper()) {
                return TaskScheduler.synchronizedContext();
            }
            return new HandlerTaskScheduler("Unkown Hanlder TaskScheduler", new Handler(myLooper));
        }
        return TaskScheduler.backgroundContext();
    }

    public static <V> Task<V> completedTask(V value) {
        TaskData<V> data = new TaskData<>();
        data.status = TaskStatus.Completed;
        data.result = value;
        return new Task<>(data);
    }

    public static <V> Task<V> cancelledTask() {
        TaskData<V> data = new TaskData<>();
        data.status = TaskStatus.Cancelled;
        return new Task<>(data);
    }

    public static <V> Task<V> faultedTask(Exception ex) {
        TaskData<V> data = new TaskData<>();
        data.exception = ex;
        data.status = TaskStatus.Faulted;
        return new Task<>(data);
    }

    @Override
    public String toString() {
        return data.status.name();
    }
}
