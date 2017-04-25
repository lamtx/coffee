package erika.redux.demo.services.communication.responses;

import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;
import erika.redux.demo.services.communication.Customer;

public class ListOfCustomerResponse extends Response {
    public final Customer[] customers;

    private ListOfCustomerResponse(Reader reader) throws MissingFieldException {
        customers = reader.readArrayObject(Customer.READER);
    }

    static final Parser<ListOfCustomerResponse> PARSER = ListOfCustomerResponse::new;
}
