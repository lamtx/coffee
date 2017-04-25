package erika.redux.demo.services.communication;

import erika.core.communication.MissingFieldException;
import erika.core.communication.ObjectReader;
import erika.core.communication.Reader;
import erika.redux.demo.models.TableStatus;

public class Table {
    public final int id;
    public final String name;
    public final TableStatus status;
    public final long price;
    public final Customer customer;

    private Table(Reader reader) throws MissingFieldException {
        status = TableStatus.fromBit(reader.readInt());
        id = reader.readInt();
        price = reader.readLong();
        name = reader.readString();
        customer = reader.readObject(Customer.READER);
    }

    @Override
    public String toString() {
        if (customer == null) {
            return status + " " + id + " " + price + " " + name;
        } else {
            return status + " " + id + " " + price + " " + name + " "
                    + customer.toString();
        }
    }

    public static final ObjectReader<Table> READER = new ObjectReader<Table>() {

        @Override
        public Table createFromReader(Reader reader) throws MissingFieldException {
            return new Table(reader);
        }

        @Override
        public Table[] newArray(int size) {
            return new Table[size];
        }
    };

}
