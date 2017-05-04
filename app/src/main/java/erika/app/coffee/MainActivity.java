package erika.app.coffee;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.IdRes;

import erika.app.coffee.action.MainActions;
import erika.app.coffee.action.PopupActions;
import erika.app.coffee.application.AppState;
import erika.app.coffee.application.BaseActivity;
import erika.app.coffee.state.MainState;
import erika.app.coffee.utility.LayoutInflaterWrapper;

public class MainActivity extends BaseActivity<MainState> {
    private final LayoutInflaterWrapper layoutInflater = new LayoutInflaterWrapper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

//    @Override
//    public Object getSystemService(@NonNull String name) {
//        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
//            return layoutInflater;
//        }
//        return super.getSystemService(name);
//    }

    @Override
    public void bindStateToView(MainState state) {
        super.bindStateToView(state);
        Class<? extends Fragment> clazz = state.backStack.peek();
        if (clazz != null) {
            bindComponentToId(clazz, R.id.container);
        } else {
            throw new IllegalStateException("Nothing in back stack");
        }
        bindComponentToId(state.popupContainer, R.id.popup);
    }

    private void bindComponentToId(Class<? extends Fragment> component, @IdRes int id) {
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
            fm.beginTransaction().replace(id, instance, null).commit();
        }
    }
}
