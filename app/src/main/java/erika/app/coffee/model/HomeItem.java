package erika.app.coffee.model;

import android.app.Fragment;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class HomeItem {
    public final Class<? extends Fragment> containerComponent;
    @StringRes
    public final int titleRes;
    @DrawableRes
    public final int iconRes;

    public HomeItem(Class<? extends Fragment> containerComponent, int titleRes, int iconRes) {
        this.containerComponent = containerComponent;
        this.titleRes = titleRes;
        this.iconRes = iconRes;
    }
}
