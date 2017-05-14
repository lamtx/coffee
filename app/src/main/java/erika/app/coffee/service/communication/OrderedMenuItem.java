package erika.app.coffee.service.communication;

import android.os.Parcel;

import erika.core.communication.MissingFieldException;
import erika.core.communication.ObjectReader;
import erika.core.communication.Reader;

public class OrderedMenuItem implements Cloneable {
    public MenuItem menuItem;
    public double quantity;
    public int id;

    @Override
    public String toString() {
        return quantity + " " + id + " " + menuItem.toString();
    }

    public OrderedMenuItem() {
    }

    public OrderedMenuItem(MenuItem menuItem, double quantity, int id) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.id = id;
    }

    public OrderedMenuItem(Parcel in) {
        menuItem = in.readParcelable(OrderedMenuItem.class.getClassLoader());
        quantity = in.readDouble();
        id = in.readInt();
    }

    public OrderedMenuItem(Reader reader) throws MissingFieldException {
        quantity = reader.readInt();
        id = reader.readInt();
        menuItem = reader.readObject(MenuItem.READER);
    }

    public static final ObjectReader<OrderedMenuItem> READER = new ObjectReader<OrderedMenuItem>() {

        @Override
        public OrderedMenuItem createFromReader(Reader reader) throws MissingFieldException {
            return new OrderedMenuItem(reader);
        }

        @Override
        public OrderedMenuItem[] newArray(int size) {
            return new OrderedMenuItem[size];
        }
    };
}
