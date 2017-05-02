package erika.app.coffee.model.args;

import erika.core.redux.Action;

public class Args implements Action {
    private final String type;

    @Override
    public String getType() {
        return type;
    }

    public Args(String type) {
        this.type = type;
    }
}
