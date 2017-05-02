package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.action.PackageManagerActions;
import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.model.args.SetComponentStatusArgs;
import erika.app.coffee.model.args.SetIsRefreshingArgs;
import erika.app.coffee.model.args.SetLoadStateArgs;
import erika.app.coffee.model.args.UpdatePackageListArgs;
import erika.app.coffee.state.PackageManagerState;
import erika.core.redux.Action;
import erika.core.redux.Reducer;
import erika.core.redux.Redux;

public class PackageManagerReducer implements Reducer<PackageManagerState> {
    @NonNull
    @Override
    public PackageManagerState reduce(@Nullable PackageManagerState state, Action action) {
        if (state == null) {
            state = new PackageManagerState();
        }
        switch (action.getType()) {
            case ActionType.UPDATE_PACKAGES_LIST:
                return updatePackageList(state, (UpdatePackageListArgs) action);
            case ActionType.SET_COMPONENT_STATUS:
                return setComponentStatus(state, (SetComponentStatusArgs) action);
            case ActionType.SET_LOAD_STATE:
                return setLoadState(state, (SetLoadStateArgs) action);
            case ActionType.SET_IS_REFRESHING:
                return setIsRefreshing(state, (SetIsRefreshingArgs) action);
            default:
                return state;
        }
    }

    private PackageManagerState setIsRefreshing(PackageManagerState state, SetIsRefreshingArgs args) {
        if (args.callingAction == PackageManagerActions.class) {
            return Redux.copy(state, x -> {
                x.refreshing = args.refreshing;
            });
        }
        return state;
    }

    private PackageManagerState updatePackageList(PackageManagerState state, UpdatePackageListArgs args) {
        return Redux.copy(state, x -> {
            x.items = args.packages;
            x.loadState = LoadState.NONE;
            x.refreshing = false;
        });
    }

    private PackageManagerState setComponentStatus(PackageManagerState state, SetComponentStatusArgs args) {
        return Redux.copy(state, x -> {
            x.items = Redux.map(state.items, y ->
                    Redux.copy(y, t -> {
                        t.components = Redux.map(t.components, u -> {
                            if (u == args.component) {
                                return Redux.copy(u, w -> {
                                    w.status = args.status;
                                });
                            }
                            return u;
                        });
                    })
            );
        });
    }

    private PackageManagerState setLoadState(PackageManagerState state, SetLoadStateArgs args) {
        if (args.callingAction == PackageManagerActions.class) {
            return Redux.copy(state, x -> {
                x.loadState = args.loadState;
            });
        }
        return state;
    }

}
