package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;

public class SetLeftPanelWidthArgs extends Args {
    public final float width;

    public SetLeftPanelWidthArgs(float width) {
        super(ActionType.SET_LEFT_PANEL_WIDTH);
        this.width = width;
    }
}
