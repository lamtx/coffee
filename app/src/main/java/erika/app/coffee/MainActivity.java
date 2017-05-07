package erika.app.coffee;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toolbar;

import erika.app.coffee.action.MainActions;
import erika.app.coffee.action.PopupActions;
import erika.app.coffee.application.AppState;
import erika.app.coffee.application.BaseActivity;
import erika.app.coffee.model.BackStackElement;
import erika.app.coffee.model.TransitionStyle;
import erika.app.coffee.state.MainState;
import erika.app.coffee.utility.LayoutInflaterWrapper;

public class MainActivity extends BaseActivity<MainState> {
    private final LayoutInflaterWrapper layoutInflater = new LayoutInflaterWrapper(this);
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
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
        toolbar.setSubtitle(state.subtitle);
    }

    @Override
    public void willReceiveState(MainState state) {
        super.willReceiveState(state);
        int num = state.backStack.size() - (getState() == null ? 0 : getState().backStack.size());
        TransitionStyle transitionStyle = num == 0 ? TransitionStyle.NONE : (num > 0 ? TransitionStyle.PUSH : TransitionStyle.POP);
        bindComponentToId(state.backStack.peek().component, R.id.container, transitionStyle);
        bindComponentToId(state.popupContainer, R.id.popup, state.popupContainer == null ? TransitionStyle.POPUP_DISMISS : TransitionStyle.POPUP_SHOW);
    }

    private void bindComponentToId(Class<? extends Fragment> component, @IdRes int id, TransitionStyle transitionStyle) {
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(id);
        if (component == null) {
            if (fragment != null) {
                fm.beginTransaction().remove(fragment).commit();
            }
        } else if (fragment == null || fragment.getClass() != component) {
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
            fragmentTransaction.replace(id, instance, null).commit();
        }
    }
}
