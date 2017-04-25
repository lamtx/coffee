package erika.core.threading;

public class TaskCompletionSource<V> {

    private final TaskData<V> data = new TaskData<>();
    private final Task<V> task = new Task<>(data);

    public TaskCompletionSource() {
    }

    public TaskCompletionSource(TaskScheduler currentScheduler) {
        data.currentScheduler = currentScheduler;
    }

    public void setResult(V value) {
        if (!trySetResult(value)) {
            throw new IllegalStateException("Task is not running");
        }
    }

    private boolean trySetResult(V value) {
        if (data.status == TaskStatus.Ready || data.status == TaskStatus.Running) {
            data.status = TaskStatus.Completed;
            data.result = value;
            data.notifyCompletion(task);
            return true;
        }
        return false;
    }

    public void setException(Exception exception) {
        if (!trySetException(exception)) {
            throw new IllegalStateException("Task is not running");
        }
    }

    private boolean trySetException(Exception exception) {
        if (data.status == TaskStatus.Ready || data.status == TaskStatus.Running) {
            data.status = TaskStatus.Faulted;
            data.exception = exception;
            data.notifyCompletion(task);
            return true;
        }
        return false;

    }

    public void setCancellation() {
        if (!trySetCancellation()) {
            throw new IllegalStateException("Task is not running");
        }
    }

    private boolean trySetCancellation() {
        if (data.status == TaskStatus.Ready || data.status == TaskStatus.Running) {
            data.status = TaskStatus.Cancelled;
            data.notifyCompletion(task);
            return true;
        }
        return false;
    }

    public Task<V> getTask() {
        return task;
    }

    public void setResultFromTask(Task<V> task) {
        if (task.isCompleted()) {
            setResult(task.getResult());
        } else if (task.isFaulted()) {
            setException(task.getException());
        } else if (task.isCancelled()) {
            setCancellation();
        }
    }
}
