package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;

public class SetAppTitleArgs extends Args {
    public final String title;
    public final String subtitle;

    public SetAppTitleArgs(String title, String subtitle) {
        super(ActionType.SET_APP_TITLE);
        this.title = title;
        this.subtitle = subtitle;
    }

}
