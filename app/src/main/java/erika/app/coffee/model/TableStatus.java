package erika.app.coffee.model;

public enum TableStatus {
    All(15),
    Available(2),
    Busy(1),
    Hengio(8),
    Tamtinh(4),
    TamtinhAndBusy(5);

    public final int bit;

    TableStatus(int bit) {
        this.bit = bit;
    }

    public boolean contains(TableStatus status) {
        return (status.bit & this.bit) != 0;
    }

    public static TableStatus fromBit(int bit) {
        for (TableStatus e : values()) {
            if (e.bit == bit) {
                return e;
            }
        }
        return null;
    }
}