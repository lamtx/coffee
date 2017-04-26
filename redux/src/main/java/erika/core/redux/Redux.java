package erika.core.redux;

import java.util.ArrayList;
import java.util.List;

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
    public static <T> List<T> map(List<T> source, ObjectCopier<T> function) {
        if (source == null) {
            return null;
        }
        List<T> result = new ArrayList<>(source.size());
        for (T e : source) {
            result.add(function.copy(e));
        }
        return result;
    }
}
