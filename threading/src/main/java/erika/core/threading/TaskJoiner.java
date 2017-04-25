package erika.core.threading;

public interface TaskJoiner<NewResult, OldResult> {
    Task<NewResult> onContinue(Task<OldResult> task) throws Exception;
}
