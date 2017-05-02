package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.state.MessageBoxState;

public class SetMessageBoxArgs extends Args {
    public final MessageBoxState state;

    public SetMessageBoxArgs(MessageBoxState state) {
        super(ActionType.SET_MESSAGE_BOX);
        this.state = state;
    }
}
