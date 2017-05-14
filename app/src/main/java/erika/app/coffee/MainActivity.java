package erika.app.coffee;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;

import java.util.HashMap;
import java.util.Map;

import erika.app.coffee.action.MainActions;
import erika.app.coffee.action.PopupActions;
import erika.app.coffee.application.AppState;
import erika.app.coffee.application.BaseActivity;
import erika.app.coffee.model.TransitionStyle;
import erika.app.coffee.service.Client;
import erika.app.coffee.state.MainState;
import erika.app.coffee.utility.LayoutInflaterWrapper;

public class MainActivity extends BaseActivity<MainState> {
    private final LayoutInflaterWrapper layoutInflater = new LayoutInflaterWrapper(this);
    private Toolbar toolbar;
    private static final Map<Class, Fragment.SavedState> states = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (getState().backStack.size() > 1) {
                dispatch(MainActions.pop());
            }
        });
    }

    @Override
    public MainState getStateFromStore(AppState appState) {
        return appState.main;
    }

    @Override
    public void onBackPressed() {
        if (getState().popupContainer != null) {
            if (getState().cancellable) {
                dispatch(PopupActions.dismiss());
            }
            return;
        }
        if (getState().backStack.size() > 1) {
            dispatch(MainActions.pop());
            return;
        }
        super.onBackPressed();
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            return layoutInflater;
        }
        return super.getSystemService(name);
    }

    @Override
    public void bindStateToView(MainState state) {
        super.bindStateToView(state);
        toolbar.setTitle(state.title);
        toolbar.setSubtitle(state.clientStatus != null && state.clientStatus != Client.Status.CONNECTED ? state.clientMessage : state.subtitle);
        toolbar.setBackgroundColor(statusToColor(state.clientStatus));
        toolbar.setNavigationIcon(state.backStack.size() > 1 ? R.drawable.ic_back : R.drawable.ic_navigation_bar_logo);
    }

    @ColorInt
    private int statusToColor(Client.Status status) {
        if (status == null) {
            status = Client.Status.CONNECTED;
        }
        switch (status) {
            case DISCONNECTED:
                return ContextCompat.getColor(this, R.color.flatui_color_pomegranate);
            case CONNECTING:
                return ContextCompat.getColor(this, R.color.flatui_color_alizarin);
            default:
                return ContextCompat.getColor(this, R.color.colorPrimary);
        }
    }

    @Override
    public void willReceiveState(MainState state) {
        super.willReceiveState(state);
        int num = state.backStack.size() - (getState() == null ? 0 : getState().backStack.size());
        TransitionStyle transitionStyle = num == 0 ? TransitionStyle.PUSH : (num > 0 ? TransitionStyle.PUSH : TransitionStyle.POP);
        bindComponentToId(state.backStack.peek().component, R.id.container, transitionStyle, true);
        bindComponentToId(state.popupContainer, R.id.popup, state.popupContainer == null ? TransitionStyle.POPUP_DISMISS : TransitionStyle.POPUP_SHOW, false);
    }

    private void bindComponentToId(Class<? extends Fragment> component, @IdRes int id, TransitionStyle transitionStyle, boolean shouldRestoreState) {
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(id);

        if (component == null) {
            if (fragment != null) {
                if (shouldRestoreState) {
                    states.put(fragment.getClass(), fm.saveFragmentInstanceState(fragment));
                }
                fm.beginTransaction().remove(fragment).commit();
            }
        } else if (fragment == null || fragment.getClass() != component) {
            if (shouldRestoreState && fragment != null) {
                states.put(fragment.getClass(), fm.saveFragmentInstanceState(fragment));
            }
            Fragment instance;
            try {
                instance = component.newInstance();
            } catch (Exception e) {
                throw new AssertionError(e);
            }
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            switch (transitionStyle) {
                case POP:
                    fragmentTransaction.setCustomAnimations(R.animator.fragment_return, R.animator.fragment_gone);
                    break;
                case PUSH:
                    fragmentTransaction.setCustomAnimations(R.animator.fragment_enter, R.animator.fragment_exit);
                    break;
                case POPUP_SHOW:
                    fragmentTransaction.setCustomAnimations(R.animator.popup_show, 0);
                    break;
                case POPUP_DISMISS:
                    fragmentTransaction.setCustomAnimations(0, R.animator.popup_dismiss);
                    break;
            }
            if (shouldRestoreState) {
                Fragment.SavedState savedState = states.get(instance.getClass());
                if (savedState != null) {
                    instance.setInitialSavedState(savedState);
                }
            }
            fragmentTransaction.replace(id, instance, null).commit();
        }
    }
}
