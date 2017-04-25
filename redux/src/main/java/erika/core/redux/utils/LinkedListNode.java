package erika.core.redux.utils;;

import android.support.annotation.Nullable;

public class LinkedListNode<E> {
    @Nullable
    LinkedListNode<E> prev;
    @Nullable
    LinkedListNode<E> next;
    final E value;

    public LinkedListNode(@Nullable LinkedListNode<E> prev, @Nullable LinkedListNode<E> next, E value) {
        this.prev = prev;
        this.next = next;
        this.value = value;
    }

    @Nullable
    public LinkedListNode<E> getNext() {
        return next;
    }

    public E getValue() {
        return value;
    }
}
