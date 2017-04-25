package erika.core.communication;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;

public class Reader {
    private String content;

    public Reader(String content) {
        this.content = content;
    }

    public boolean canRead() {
        return !TextUtils.isEmpty(content);
    }

    private String readNext() throws MissingFieldException {
        if (TextUtils.isEmpty(content)) {
            throw new MissingFieldException();
        }
        int index = content.indexOf(',');
        if (index != -1) {
            String value = content.substring(0, index);
            content = content.substring(index + 1);
            return value;
        } else {
            String value = content;
            content = null;
            return value;
        }
    }

    public long readLong() throws MissingFieldException {
        String value = readNext();
        if (value != null) {
            return Long.parseLong(value);
        }
        return 0L;
    }

    public int readInt() throws MissingFieldException {
        String value = readNext();
        if (value != null) {
            return Integer.parseInt(value);
        }
        return 0;
    }

    public double readDouble() throws MissingFieldException {
        String value = readNext();
        if (value != null) {
            return Double.parseDouble(value);
        }
        return 0;
    }

    public boolean readBoolean() throws MissingFieldException {
        String value = readNext();
        return "1".equals(value);
    }

    public String readString() throws MissingFieldException {
        return StringDecoder.decode(readNext());
    }

    public Date readDate() throws MissingFieldException {
        long milliseconds = readLong();
        if (milliseconds == Long.MIN_VALUE) {
            return null;
        }
        return new Date(milliseconds);
    }

    public <T> T readObject(ObjectReader<T> reader) throws MissingFieldException {
        if (readBoolean()) {
            return reader.createFromReader(this);
        }
        return null;
    }

    public <T> T[] readArrayObject(ObjectReader<T> reader) throws MissingFieldException {
        boolean hasValue = readBoolean();
        if (hasValue) {
            T[] value = reader.newArray(readInt());
            for (int i = 0; i < value.length; i++) {
                value[i] = reader.createFromReader(this);
            }
            return value;
        }
        return null;
    }

    public String[] readStringArray() throws MissingFieldException {
        boolean hasValue = readBoolean();
        if (hasValue) {
            String[] value = new String[readInt()];
            for (int i = 0; i < value.length; i++) {
                value[i] = readString();
            }
            return value;
        }
        return null;
    }

    public <T> ArrayList<T> readArrayListObject(ObjectReader<T> reader) throws MissingFieldException {
        boolean hasValue = readBoolean();
        if (hasValue) {
            int len = readInt();
            ArrayList<T> array = new ArrayList<>(len);
            for (int i = 0; i < len; i++) {
                array.add(reader.createFromReader(this));
            }
            return array;
        }
        return null;
    }
}
