package erika.redux.demo.services.communication.requests;

import erika.core.communication.Writer;

public class NumberItemOfMenuRequest extends Request {
    public NumberItemOfMenuRequest(int menuId) {
        this.menuId = menuId;
    }

    public int menuId;

    @Override
    public void writeToWriter(Writer writer) {
        super.writeToWriter(writer);
        writer.write(menuId);
    }
}
