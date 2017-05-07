package erika.app.coffee.model;

import android.app.Fragment;

public class BackStackElement {
    public final Class<? extends Fragment> component;
    public final String title;
    public final String subtitle;

    public BackStackElement(Class<? extends Fragment> component) {
        this(component, null, null);
    }

    public BackStackElement(Class<? extends Fragment> component, String title) {
        this(component, title, null);
    }

    public BackStackElement(Class<? extends Fragment> component, String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
        this.component = component;
    }
}
