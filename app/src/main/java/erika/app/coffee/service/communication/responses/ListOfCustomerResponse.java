package erika.app.coffee.service.communication.responses;

import erika.app.coffee.service.communication.Customer;
import erika.core.communication.MissingFieldException;
import erika.core.communication.Parser;
import erika.core.communication.Reader;

public class ListOfCustomerResponse extends Response {
    public final Customer[] customers;

    private ListOfCustomerResponse(Reader reader) throws MissingFieldException {
        customers = reader.readArrayObject(Customer.READER);
    }

    static final Parser<ListOfCustomerResponse> PARSER = ListOfCustomerResponse::new;
}
