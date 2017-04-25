package erika.redux.demo;

import erika.redux.demo.application.AppState;
import erika.redux.demo.application.BaseFragment;
import erika.redux.demo.states.HomeState;

public class HomeFragment extends BaseFragment<HomeState> {
    @Override
    public HomeState getStateFromStore(AppState appState) {
        return appState.home;
    }
}
