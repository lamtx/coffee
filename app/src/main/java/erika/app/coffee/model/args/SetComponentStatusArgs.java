package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.Component;
import erika.app.coffee.model.ComponentStatus;

public class SetComponentStatusArgs extends Args {
    public final Component component;
    public final ComponentStatus status;

    public SetComponentStatusArgs(Component component, ComponentStatus status) {
        super(ActionType.SET_COMPONENT_STATUS);
        this.component = component;
        this.status = status;
    }
}
