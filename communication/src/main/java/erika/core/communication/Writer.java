package erika.core.communication;

import java.util.Date;
import java.util.List;

public final class Writer {

    private final StringBuilder sb = new StringBuilder();

    public Writer write(int value) {
        appendSeparator();
        sb.append(value);
        return this;
    }

    public Writer write(long value) {
        appendSeparator();
        sb.append(value);
        return this;
    }

    public Writer write(boolean value) {
        appendSeparator();
        sb.append(value ? "1" : "0");
        return this;
    }

    public Writer write(String value) {
        appendSeparator();
        if (value != null) {
            sb.append(StringDecoder.encode(value));
        }
        return this;
    }

    public Writer write(Date value) {
        long milliseconds = value == null ? Long.MIN_VALUE : value.getTime();
        write(milliseconds);
        return this;
    }

    public Writer write(double value) {
        appendSeparator();
        sb.append(value);
        return this;
    }
//
//    public Writer write(Writable value) {
//        write(value != null);
//        if (value != null) {
//            value.writeToWriter(this);
//        }
//        return this;
//    }
//
//    public Writer write(Writable[] value) {
//        write(value != null);
//        if (value != null) {
//            write(value.length);
//            for (Writable obj : value) {
//                obj.writeToWriter(this);
//            }
//        }
//        return this;
//    }

    public Writer write(String[] value) {
        write(value != null);
        if (value != null) {
            write(value.length);
            for (String obj : value) {
                write(obj);
            }
        }
        return this;
    }

//    public <T extends Writable> Writer write(List<T> value) {
//        write(value != null);
//        if (value != null) {
//            write(value.size());
//            for (Writable obj : value) {
//                obj.writeToWriter(this);
//            }
//        }
//        return this;
//    }

    public Writer write(int[] value) {
        write(value != null);
        if (value != null) {
            write(value.length);
            for (int i : value) {
                write(i);
            }
        }
        return this;
    }

    String toMessage() {
        return sb.toString();
    }

    @Override
    public String toString() {
        return toMessage();
    }

    private boolean hasValue = false;

    private void appendSeparator() {
        if (hasValue) {
            sb.append(',');
        } else {
            hasValue = true;
        }
    }
}
