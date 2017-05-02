package erika.app.coffee.service.communication;

import erika.core.communication.MissingFieldException;
import erika.core.communication.ObjectReader;
import erika.core.communication.Reader;

public class MenuItem {
    public int id;
    public String name;
    public int percent;
    public long price;
    public String unit;

    public MenuItem() {
    }

    public MenuItem(int id, String name, long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private MenuItem(Reader reader) throws MissingFieldException {
        id = reader.readInt();
        price = reader.readLong();
        percent = reader.readInt();
        name = reader.readString();
        unit = reader.readString();
    }

    public static final ObjectReader<MenuItem> READER = new ObjectReader<MenuItem>() {

        @Override
        public MenuItem createFromReader(Reader reader) throws MissingFieldException {
            return new MenuItem(reader);
        }

        @Override
        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };
}
