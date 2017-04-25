package erika.redux.demo.services.communication;

import android.os.Parcel;

import erika.core.communication.MissingFieldException;
import erika.core.communication.ObjectReader;
import erika.core.communication.Reader;

public class OrderedMenuItem {
    public MenuItem menuItem;
    public int quantity;
    public int detailId;
    public String note;

    public long getTotal() {

        return (long) (quantity * menuItem.getPrice()
                * (100 - menuItem.getPercent()) / 100);

    }

    @Override
    public String toString() {
        return quantity + " " + detailId + " " + menuItem.toString();
    }

    public OrderedMenuItem() {
    }

    public OrderedMenuItem(MenuItem menuItem, int quantity, int detailId,
            String note) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.detailId = detailId;
        this.note = note;
    }

    public OrderedMenuItem(Parcel in) {
        menuItem = in.readParcelable(OrderedMenuItem.class.getClassLoader());
        quantity = in.readInt();
        detailId = in.readInt();
        note = in.readString();
    }

    public OrderedMenuItem(Reader reader) throws MissingFieldException {
        quantity = reader.readInt();
        detailId = reader.readInt();
        note = reader.readString();
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
