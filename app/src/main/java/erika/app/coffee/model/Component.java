package erika.app.coffee.model;

import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;

import erika.core.redux.CloneableObject;

public class Component implements CloneableObject {

    public enum Type {
        ACTIVITY, SERVICE, RECEIVER, PROVIDER
    }

    public final ComponentInfo info;
    public final Type type;
    public ComponentStatus status;
    public CharSequence label;

    public Component(ComponentInfo info, Type type, PackageManager pm) {
        this.info = info;
        this.type = type;
        int enabledStatus = pm.getComponentEnabledSetting(new ComponentName(info.packageName, info.name));
        switch (enabledStatus) {
            case PackageManager.COMPONENT_ENABLED_STATE_DISABLED:
                status = ComponentStatus.DISABLED;
                break;
            case PackageManager.COMPONENT_ENABLED_STATE_ENABLED:
                status = ComponentStatus.ENABLED;
                break;
            case PackageManager.COMPONENT_ENABLED_STATE_DEFAULT:
            default:
                status = ComponentStatus.DEFAULT;
        }
        label = info.loadLabel(pm);
    }

    public Component(ActivityInfo activity, boolean isBroadcastReceiver, PackageManager pm) {
        this(activity, isBroadcastReceiver ? Type.RECEIVER : Type.ACTIVITY, pm);
    }

    public Component(ServiceInfo service, PackageManager pm) {
        this(service, Type.SERVICE, pm);
    }

    public Component(ProviderInfo provider, PackageManager pm) {
        this(provider, Type.PROVIDER, pm);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
