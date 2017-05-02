package erika.app.coffee.presentation;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class ViewBinder<T> extends RecyclerView.ViewHolder {

    private T item;

    public ViewBinder(View view) {
        super(view);
    }

    public abstract void bind();

    public void setItem(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }

}
