package erika.app.coffee.state;


import erika.app.coffee.model.PackageFilter;
import erika.app.coffee.model.Package;

public class PackageManagerState extends BaseListState<Package> {
    public PackageFilter filter = new PackageFilter();

}
