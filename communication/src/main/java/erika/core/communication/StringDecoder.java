package erika.core.communication;

class StringDecoder {
    private static final char[][] map =
            { { '\n', '\u2FC2' }, { '\r', '\u2FC4', }, { ',', '\u2FC3' } };

    static String encode(String input) {
        if (input != null && input.length() != 0) {
            char[] chars = input.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                for (char[] e : map) {
                    if (chars[i] == e[0]) {
                        chars[i] = e[1];
                    }
                }
            }
            return new String(chars);
        }
        return input;
    }

    static String decode(String input) {
        if (input != null && input.length() != 0) {
            char[] chars = input.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                for (char[] e : map) {
                    if (chars[i] == e[1]) {
                        chars[i] = e[0];
                    }
                }
            }
            return new String(chars);
        }
        return input;
    }
}
