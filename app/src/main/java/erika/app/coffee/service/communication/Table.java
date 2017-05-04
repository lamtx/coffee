package erika.app.coffee.service.communication;

import erika.app.coffee.model.TableStatus;
import erika.core.communication.MissingFieldException;
import erika.core.communication.ObjectReader;
import erika.core.communication.Reader;

public class Table {
    public final int id;
    public final String name;
    public final TableStatus status;
    public final long price;

    public Table(int id, String name, TableStatus status, long price) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.price = price;
    }

    private Table(Reader reader) throws MissingFieldException {
        status = TableStatus.values()[reader.readInt()]; // TODO: Check safe cast int to enum
        id = reader.readInt();
        price = reader.readLong();
        name = reader.readString();
    }

    @Override
    public String toString() {
        return status + " " + id + " " + price + " " + name;
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
