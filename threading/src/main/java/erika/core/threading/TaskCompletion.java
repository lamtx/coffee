package erika.core.threading;

public interface TaskCompletion<V> {
    void onContinue(Task<V> task) throws Exception;
}