package erika.core.extension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamExtension {
    public static String readString(InputStream streamIn, String encoding, long estimatedSize) throws IOException {
        if (encoding == null) {
            encoding = "UTF-8";
        }
        byte[] bytes = readAll(streamIn, estimatedSize, true);
        return new String(bytes, encoding);
    }

    public static void copyTo(InputStream source, OutputStream destination) throws IOException {
        byte[] buffer = new byte[10024];
        int len;
        try {
            while ((len = source.read(buffer, 0, buffer.length)) > 0) {
                destination.write(buffer, 0, len);
            }
        } finally {
            destination.close();
        }
    }

    public static byte[] readAll(InputStream streamIn, long estimatedSize, boolean keepOpen) throws IOException {
        if (estimatedSize <= 0) {
            estimatedSize = 1024;
        }
        byte[] buffer = new byte[(int) estimatedSize];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len;
        try {
            while ((len = streamIn.read(buffer, 0, buffer.length)) > 0) {
                out.write(buffer, 0, len);
            }
            return out.toByteArray();
        } finally {
            if (!keepOpen) {
                out.close();
            }
        }
    }
}
