package erika.core.net.datacontract;

public interface DataCreator<T> {
    T createFromJson(JsonHelper helper);

    T[] newArray(int size);

}
