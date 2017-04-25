package erika.core.redux;

public interface CloneableObject extends Cloneable {
    Object clone() throws CloneNotSupportedException;
}
