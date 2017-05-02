package erika.app.coffee.model.args;

import java.util.List;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.Package;

public class UpdatePackageListArgs extends Args {
    public final List<Package> packages;

    public UpdatePackageListArgs(List<Package> packages) {
        super(ActionType.UPDATE_PACKAGES_LIST);
        this.packages = packages;
    }
}
