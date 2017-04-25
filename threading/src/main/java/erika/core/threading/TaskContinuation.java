package erika.core.threading;

public interface TaskContinuation<NewResult, OldResult> {
    NewResult onContinue(Task<OldResult> task) throws Exception;
}
