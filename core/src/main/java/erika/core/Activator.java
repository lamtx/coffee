package erika.core;

public interface Activator<HostType> extends Constructor<HostType> {

    HostType[] newArray(int size);
}