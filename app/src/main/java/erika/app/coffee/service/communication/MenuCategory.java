package erika.app.coffee.service.communication;

import java.util.ArrayList;
import java.util.List;

import erika.core.communication.MissingFieldException;
import erika.core.communication.ObjectReader;
import erika.core.communication.Reader;

public class MenuCategory {
    public final String name;
    public final List<MenuItem> items;

    public MenuCategory(String name, List<MenuItem> items) {
        this.name = name;
        this.items = items;
    }

    private MenuCategory(Reader reader) throws MissingFieldException {
        name = reader.readString();
        items = reader.readArrayListObject(MenuItem.READER);
    }

    public static final ObjectReader<MenuCategory> READER = new ObjectReader<MenuCategory>() {

        @Override
        public MenuCategory createFromReader(Reader reader) throws MissingFieldException {
            return new MenuCategory(reader);
        }

        @Override
        public MenuCategory[] newArray(int size) {
            return new MenuCategory[size];
        }
    };
}
