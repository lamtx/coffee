package erika.app.coffee.application;

import erika.core.redux.Store;

public class AppStore extends Store<AppState> {
    public AppStore() {
        super(new AppReducer());
    }
}
