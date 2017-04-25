package erika.core.threading;

import java.util.concurrent.CancellationException;

final class TaskData<V> {
    TaskStatus status = TaskStatus.Ready;
    Exception exception;
    V result;
    @ContinuationOptions
    int taskContinuationOptions;
    TaskScheduler scheduler;
    ContinuationTask<V> continuationTask;
    TaskScheduler currentScheduler;

    public TaskData<V> retain() {
        TaskData<V> instance = new TaskData<>();
        instance.status = status;
        instance.exception = exception;
        instance.result = result;
        instance.currentScheduler = currentScheduler;
        return instance;
    }

    void notifyCompletion(final Task<V> task) {
        if (continuationTask == null) {
            return;
        }

        final ContinuationTask<V> ct = continuationTask;
        continuationTask = null;

        if ((status == TaskStatus.Cancelled && (taskContinuationOptions & ContinuationOptions.NOT_ON_CANCELLED) != 0)
                || (status == TaskStatus.Faulted && (taskContinuationOptions & ContinuationOptions.NOT_ON_FAULTED) != 0)) {
            return;
        }
        if ((taskContinuationOptions & ContinuationOptions.EXECUTE_SYNCHRONOUSLY) != 0) {
            ct.onContinue(task);
        } else {
            scheduler.post(new Runnable() {
                @Override
                public void run() {
                    ct.onContinue(task);
                }
            });
        }
    }
}

interface ContinuationTask<OldType> {
    void onContinue(Task<OldType> task);
}

class ContinueTask<NewType, OldType> implements ContinuationTask<OldType> {
    private final TaskContinuation<NewType, OldType> action;
    private final TaskCompletionSource<NewType> taskCompletionSource;

    ContinueTask(TaskContinuation<NewType, OldType> action, TaskScheduler currentScheduler) {
        this.action = action;
        taskCompletionSource = new TaskCompletionSource<>(currentScheduler);
    }

    Task<NewType> getTask() {
        return taskCompletionSource.getTask();
    }

    @Override
    public void onContinue(Task<OldType> task) {
        final NewType value;
        try {
            value = action.onContinue(task);
        } catch (CancellationException e) {
            taskCompletionSource.setCancellation();
            return;
        } catch (Exception e) {
            taskCompletionSource.setException(e);
            return;
        }
        taskCompletionSource.setResult(value);
    }
}

class JoinTask<NewType, OldType> implements ContinuationTask<OldType> {
    private final TaskJoiner<NewType, OldType> action;
    private final TaskCompletionSource<NewType> taskCompletionSource;

    JoinTask(TaskJoiner<NewType, OldType> action, TaskScheduler currentScheduler) {
        this.action = action;
        taskCompletionSource = new TaskCompletionSource<>(currentScheduler);
    }

    Task<NewType> getTask() {
        return taskCompletionSource.getTask();
    }

    @Override
    public void onContinue(Task<OldType> task) {
        final Task<NewType> value;
        try {
            value = action.onContinue(task);
        } catch (CancellationException e) {
            taskCompletionSource.setCancellation();
            return;
        } catch (Exception e) {
            taskCompletionSource.setException(e);
            return;
        }
        value.then(new TaskCompletion<NewType>() {
            @Override
            public void onContinue(Task<NewType> task) throws Exception {
                taskCompletionSource.setResultFromTask(task);
            }
        }, ContinuationOptions.EXECUTE_SYNCHRONOUSLY);
    }
}

class TaskCompletionWrapper<OldResult, NewResult> implements TaskContinuation<NewResult, OldResult> {
    private final TaskCompletion<OldResult> base;

    TaskCompletionWrapper(TaskCompletion<OldResult> base) {
        this.base = base;
    }

    @Override
    public NewResult onContinue(Task<OldResult> task) throws Exception {
        base.onContinue(task);
        return null;
    }
}