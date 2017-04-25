package erika.redux.demo.services.communication;

import erika.core.communication.MissingFieldException;
import erika.core.communication.ObjectReader;
import erika.core.communication.Reader;

public class MenuCategory {
    public final String name;
    public final MenuItem[] items;

    private MenuCategory(Reader reader) throws MissingFieldException {
        name = reader.readString();
        items = reader.readArrayObject(MenuItem.READER);
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
