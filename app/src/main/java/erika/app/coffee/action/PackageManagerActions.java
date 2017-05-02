package erika.app.coffee.action;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import erika.app.coffee.model.Component;
import erika.app.coffee.model.ComponentStatus;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.model.Package;
import erika.app.coffee.model.PackageFilter;
import erika.app.coffee.model.args.SetComponentStatusArgs;
import erika.app.coffee.model.args.SetIsRefreshingArgs;
import erika.app.coffee.model.args.SetLoadStateArgs;
import erika.app.coffee.model.args.UpdatePackageListArgs;
import erika.core.Arrays;
import erika.core.redux.Action;
import erika.core.redux.DispatchAction;
import erika.core.threading.TaskFactory;

public class PackageManagerActions {

    public static DispatchAction fetchPackages(Context context, PackageFilter filter) {
        return dispatcher -> {
            dispatcher.dispatch(setLoadState(LoadState.LOADING));
            TaskFactory.startNew(() -> {
                PackageManager pm = context.getPackageManager();
                List<PackageInfo> packages = pm.getInstalledPackages(0);
                return Arrays.map(packages, x -> {
                    List<Component> components = new ArrayList<>();
                    if (filter.components.isEmpty() || filter.components.contains(Component.Type.ACTIVITY)) {
                        for (ComponentInfo activity : loadComponents(pm, x, PackageManager.GET_ACTIVITIES)) {
                            components.add(new Component(activity, Component.Type.ACTIVITY, pm));
                        }
                    }
                    if (filter.components.isEmpty() || filter.components.contains(Component.Type.SERVICE)) {
                        for (ComponentInfo activity : loadComponents(pm, x, PackageManager.GET_SERVICES)) {
                            components.add(new Component(activity, Component.Type.SERVICE, pm));
                        }
                    }
                    if (filter.components.isEmpty() || filter.components.contains(Component.Type.PROVIDER)) {
                        for (ComponentInfo activity : loadComponents(pm, x, PackageManager.GET_PROVIDERS)) {
                            components.add(new Component(activity, Component.Type.PROVIDER, pm));
                        }
                    }
                    if (filter.components.isEmpty() || filter.components.contains(Component.Type.RECEIVER)) {
                        for (ComponentInfo activity : loadComponents(pm, x, PackageManager.GET_RECEIVERS)) {
                            components.add(new Component(activity, Component.Type.RECEIVER, pm));
                        }
                    }
                    return new Package(x, components, pm);
                });
            }).then(task -> {
                dispatcher.dispatch(setPackages(task.getResult()));
            });
        };
    }

    @NonNull
    private static ComponentInfo[] loadComponents(PackageManager pm, PackageInfo packageInfo, int component) {
        ComponentInfo[] empty = new ComponentInfo[0];
        try {
            PackageInfo newPackageInfo = pm.getPackageInfo(packageInfo.packageName, component);
            switch (component) {
                case PackageManager.GET_ACTIVITIES:
                    return newPackageInfo.activities == null ? empty : newPackageInfo.activities;
                case PackageManager.GET_SERVICES:
                    return newPackageInfo.services == null ? empty : newPackageInfo.services;
                case PackageManager.GET_RECEIVERS:
                    return newPackageInfo.receivers == null ? empty : newPackageInfo.receivers;
                case PackageManager.GET_PROVIDERS:
                    return newPackageInfo.providers == null ? empty : newPackageInfo.providers;
                default:
                    throw new IllegalArgumentException("component");
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError(e);
        }
    }

    public static Action setPackages(List<Package> items) {
        return new UpdatePackageListArgs(items);
    }

    public static DispatchAction disableComponent(Context context, Component component, boolean disabled) {
        return dispatcher -> {
            PackageManager pm = context.getPackageManager();
            ComponentName cn = new ComponentName(component.info.packageName, component.info.name);
            pm.setComponentEnabledSetting(cn, disabled ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED : PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            dispatcher.dispatch(setComponentStatus(component, disabled ? ComponentStatus.DISABLED : ComponentStatus.ENABLED));
        };
    }

    public static Action setComponentStatus(Component component, ComponentStatus status) {
        return new SetComponentStatusArgs(component, status);
    }

    public static Action setLoadState(LoadState loadState) {
        return new SetLoadStateArgs(PackageManagerActions.class, loadState);
    }

    public static Action setIsRefreshing(boolean refreshing) {
        return new SetIsRefreshingArgs(PackageManagerActions.class, refreshing);
    }
}
