package erika.app.coffee.state;

import java.util.List;

import erika.app.coffee.service.communication.MenuCategory;

public class MenuState extends BaseListState<MenuCategory> {
    public List<MenuCategory> noneFilteredItems;
    public String keyword;
    public int tableId;
}
