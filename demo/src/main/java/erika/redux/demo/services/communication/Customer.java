package erika.redux.demo.services.communication;

import android.os.Parcel;

import erika.core.communication.MissingFieldException;
import erika.core.communication.ObjectReader;
import erika.core.communication.Reader;

public class Customer {
    public static final String EmpyCardNo = "*";
    public String name;
    public int id;
    public String cardNo;

    public Customer(String name, String cardNo, int id) {
        this.name = name;
        this.id = id;
        this.cardNo = cardNo;
    }

    private Customer(Reader reader) throws MissingFieldException {
        id = reader.readInt();
        name = reader.readString();
        cardNo = reader.readString();
    }

    @Override
    public String toString() {
        return id + " " + cardNo + " " + name;
    }

    public static final ObjectReader<Customer> READER = new ObjectReader<Customer>() {

        @Override
        public Customer createFromReader(Reader reader) throws MissingFieldException {
            return new Customer(reader);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };
}
