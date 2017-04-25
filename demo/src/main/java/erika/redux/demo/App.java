package erika.redux.demo;

import android.support.annotation.NonNull;

import erika.core.redux.ReduxApplication;
import erika.redux.demo.application.AppState;
import erika.redux.demo.application.AppStore;

public class App extends ReduxApplication<AppState> {

    @NonNull
    @Override
    public AppStore createStore() {
        return new AppStore();
    }


}
