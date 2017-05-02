package erika.app.coffee.service.communication.requests;

import erika.app.coffee.model.TableStatus;
import erika.core.communication.Writer;

public class ListOfTableRequest extends Request {
    private TableStatus status;
    public boolean includeOutside = false;
    public String keyword;

    public TableStatus getStatus() {
        return status;
    }

    public ListOfTableRequest(TableStatus status) {
        this(status, false, null);
    }

    public ListOfTableRequest(TableStatus status, boolean includeOutside) {
        this(status, includeOutside, null);
    }

    public ListOfTableRequest(TableStatus tableStatus, boolean includeOutside, String keyword) {
        this.status = tableStatus;
        this.includeOutside = includeOutside;
        this.keyword = keyword;
    }

    @Override
    public void writeToWriter(Writer writer) {
        super.writeToWriter(writer);
        writer.write(includeOutside).write(status.bit).write(keyword);
    }

}
