package erika.app.coffee.model;

import erika.core.redux.immutable.ImmutableEnumSet;


public class PackageFilter implements Cloneable{

    public enum Category {
        LAUNCHER, HOME, ALL
    }

    public enum Action {
        MAIN, VIEW, EDIT, ALL
    }

    public ImmutableEnumSet<Component.Type> components = ImmutableEnumSet.noneOf(Component.Type.class);
    public ImmutableEnumSet<Category> categories = ImmutableEnumSet.noneOf(Category.class);
    public ImmutableEnumSet<Action> actions = ImmutableEnumSet.noneOf(Action.class);

}
