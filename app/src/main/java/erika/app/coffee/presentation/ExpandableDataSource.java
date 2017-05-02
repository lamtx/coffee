package erika.app.coffee.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.Collections;
import java.util.List;

public abstract class ExpandableDataSource<ParentType, ChildType>  extends BaseExpandableListAdapter {
    public interface HeaderViewBinderCreator<ParentType> {
        ViewBinder<ParentType> createHeaderView(LayoutInflater inflater, ViewGroup parent, int groupPosition);
    }

    public interface ViewBinderCreator<ChildType> {
        ViewBinder<ChildType> createView(LayoutInflater inflater, ViewGroup parent, int position);
    }

    private List<ParentType> parents;
    private final HeaderViewBinderCreator<ParentType> headerViewBinderCreator;
    private final ViewBinderCreator<ChildType> viewBinderCreator;

    public ExpandableDataSource(List<ParentType> parents, HeaderViewBinderCreator<ParentType> headerViewBinderCreator, ViewBinderCreator<ChildType> viewBinderCreator) {
        this.parents = parents == null ? Collections.emptyList() : parents;
        this.headerViewBinderCreator = headerViewBinderCreator;
        this.viewBinderCreator = viewBinderCreator;
    }

    @Override
    public int getGroupCount() {
        return parents.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return getChildren(getGroup(groupPosition)).size();
    }

    protected abstract List<ChildType> getChildren(ParentType parent);

    @Override
    public ParentType getGroup(int groupPosition) {
        return parents.get(groupPosition);
    }

    @Override
    public ChildType getChild(int groupPosition, int childPosition) {
        return getChildren(getGroup(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return (long)groupPosition << 32 | (long)childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ViewBinder<ParentType> viewBinder;
        if (convertView == null) {
            viewBinder = headerViewBinderCreator.createHeaderView(LayoutInflater.from(parent.getContext()), parent, groupPosition);
            viewBinder.itemView.setTag(viewBinder);
        } else {
            viewBinder = ((ViewBinder<ParentType>) convertView.getTag());
        }
        viewBinder.setItem(getGroup(groupPosition));
       // viewBinder.setPosition(groupPosition);
        viewBinder.bind();
        return viewBinder.itemView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewBinder<ChildType> viewBinder;
        if (convertView == null) {
            viewBinder = viewBinderCreator.createView(LayoutInflater.from(parent.getContext()), parent, childPosition);
            viewBinder.itemView.setTag(viewBinder);
        } else {
            viewBinder = ((ViewBinder<ChildType>) convertView.getTag());
        }
        viewBinder.setItem(getChild(groupPosition, childPosition));
        //viewBinder.setPosition(groupPosition);
        viewBinder.bind();
        return viewBinder.itemView;
    }

    public void setParents(List<ParentType> parents) {
        if (parents == null) {
            parents = Collections.emptyList();
        }
        if (this.parents != parents) {
            this.parents = parents;
            notifyDataSetChanged();
        }
    }
}
