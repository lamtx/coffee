package erika.app.coffee.model;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

import erika.core.redux.CloneableObject;

public class Package implements CloneableObject {
    public final PackageInfo info;
    public ComponentStatus status;
    public List<Component> components;
    public final CharSequence label;

    public Package(PackageInfo info, List<Component> components, PackageManager pm) {
        this.info = info;
        this.status = ComponentStatus.DEFAULT;
        this.components = components;
        this.label = info.applicationInfo.loadLabel(pm);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
