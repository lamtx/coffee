package erika.core.redux;

public class Redux {
    private Redux() {
    }

    public static <T extends CloneableObject> T clone(T old, ObjectModifier<T> modifier) {
        T clone;
        try {
            clone = (T) old.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        modifier.modify(clone);
        return clone;
    }
}
