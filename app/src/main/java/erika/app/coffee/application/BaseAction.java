package erika.app.coffee.application;

import erika.core.redux.Action;

public class BaseAction implements Action {
    private final String type;

    public BaseAction(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }
}
