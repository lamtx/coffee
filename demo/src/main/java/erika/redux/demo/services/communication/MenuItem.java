package erika.redux.demo.services.communication;

import erika.core.communication.MissingFieldException;
import erika.core.communication.ObjectReader;
import erika.core.communication.Reader;

public class MenuItem {
    public int id;
    public String name;
    public String code;
    public long price;
    public int percent;
    public int quantity;
    public String englishName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getPercent() {
        return this.percent;
    }

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
        code = reader.readString();
        englishName = reader.readString();
        percent = reader.readInt();
        quantity = reader.readInt();
        name = reader.readString();
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
