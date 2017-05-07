package erika.app.coffee.presentation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Support binding item to view
 *
 * @param <T>
 */
public class DataSource<T> extends RecyclerView.Adapter<ViewBinder<T>> {


    public interface ViewBinderCreator<T> {
        ViewBinder<T> createView(LayoutInflater inflater, ViewGroup parent, int viewType);
    }

    public interface GetViewType<T> {
        int typeOfItem(T item);
    }

    private List<T> items;
    private final ViewBinderCreator<T> viewBinderCreator;
    private final GetViewType<T> getViewType;

    public DataSource(ViewBinderCreator<T> viewBinderCreator) {
        this(null, viewBinderCreator, null);
    }

    public DataSource(List<T> items, ViewBinderCreator<T> viewBinderCreator) {
        this(items, viewBinderCreator, null);
    }

    public DataSource(ViewBinderCreator<T> viewBinderCreator, GetViewType<T> getViewType) {
        this(null, viewBinderCreator, getViewType);
    }

    public DataSource(List<T> items, ViewBinderCreator<T> viewBinderCreator, GetViewType<T> getViewType) {
        if (items == null) {
            items = Collections.emptyList();
        }
        this.items = items;
        this.viewBinderCreator = viewBinderCreator;
        this.getViewType = getViewType;
    }

    @Override
    public int getItemViewType(int position) {
        if (getViewType != null) {
            return getViewType.typeOfItem(items.get(position));
        }
        return super.getItemViewType(position);
    }

    @Override
    public ViewBinder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewBinderCreator.createView(LayoutInflater.from(parent.getContext()), parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewBinder<T> holder, int position) {
        holder.setItem(items.get(position));
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<T> items) {
        if (items == null) {
            items = Collections.emptyList();
        }
        if (items == this.items) {
            return;
        }
        if (items.size() == this.items.size()) {
            // Detect item changed
            int changedIndex = -1;
            for (int i = 0; i < items.size(); i++) {
                if (this.items.get(i) != items.get(i)) {
                    if (changedIndex == -1) {
                        changedIndex = i;
                    } else {
                        this.items = items;
                        notifyDataSetChanged();
                        return;
                    }
                }
            }
            if (changedIndex != -1) {
                this.items = items;
                notifyItemChanged(changedIndex);
            } else {
                this.items = items;
                notifyDataSetChanged();
            }
        } else if (items.size() > this.items.size()) {
            // Detect item added
            int added = items.size() - this.items.size();
            int changedIndex = this.items.size();
            for (int i = 0; i < this.items.size(); i++) {
                if (this.items.get(i) != items.get(i)) {
                    changedIndex = i;
                    break;
                }
            }
            int oldIndex = changedIndex;
            int newIndex = oldIndex + added;
            for (; oldIndex < this.items.size(); oldIndex++, newIndex++) {
                if (this.items.get(oldIndex) != items.get(newIndex)) {
                    this.items = items;
                    notifyDataSetChanged();
                    return;
                }
            }
            this.items = items;
            notifyItemRangeInserted(changedIndex, added);
        } else {
            // Detect item removed
            int removed = this.items.size() - items.size();
            int changedIndex = items.size();
            for (int i = 0; i < items.size(); i++) {
                if (this.items.get(i) != items.get(i)) {
                    changedIndex = i;
                    break;
                }
            }
            int newIndex = changedIndex;
            int oldIndex = changedIndex + removed;
            for (; oldIndex < this.items.size(); oldIndex++, newIndex++) {
                if (this.items.get(oldIndex) != items.get(newIndex)) {
                    this.items = items;
                    notifyDataSetChanged();
                    return;
                }
            }
            this.items = items;
            notifyItemRangeRemoved(changedIndex, removed);
        }
    }
}
