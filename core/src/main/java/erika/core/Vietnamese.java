package erika.core;

/// <summary>
/// Version: 1.4
/// </summary>
public class Vietnamese {
    private static final char[] normal;
    private static final char[][] tones = {
            // sắc huyền hỏi ngã nặng
            { 'a', 'á', 'à', 'ả', 'ã', 'ạ', 'â', 'ấ', 'ầ', 'ẩ', 'ẫ', 'ậ', 'ă', 'ắ', 'ằ', 'ẳ', 'ẵ', 'ặ', 'Á', 'À', 'Ả', 'Ã', 'Ạ', 'Â', 'Ấ', 'Ầ', 'Ẩ', 'Ẫ', 'Ậ', 'Ă', 'Ắ', 'Ằ', 'Ẳ', 'Ẵ', 'Ặ' },
            { 'e', 'é', 'è', 'ẻ', 'ẽ', 'ẹ', 'ê', 'ế', 'ề', 'ể', 'ễ', 'ệ', 'É', 'È', 'Ẻ', 'Ẽ', 'Ẹ', 'Ê', 'Ế', 'Ề', 'Ể', 'Ễ', 'Ệ' },
            { 'i', 'í', 'ì', 'ỉ', 'ĩ', 'ị', 'Í', 'Ì', 'Ỉ', 'Ĩ', 'Ị' },
            { 'o', 'ó', 'ò', 'ỏ', 'õ', 'ọ', 'ô', 'ố', 'ồ', 'ổ', 'ỗ', 'ộ', 'ơ', 'ớ', 'ờ', 'ở', 'ỡ', 'ợ', 'Ó', 'Ò', 'Ỏ', 'Õ', 'Ọ', 'Ô', 'Ố', 'Ồ', 'Ổ', 'Ỗ', 'Ộ', 'Ơ', 'Ớ', 'Ờ', 'Ở', 'Ỡ', 'Ợ' },
            { 'u', 'ú', 'ù', 'ủ', 'ũ', 'ụ', 'ư', 'ứ', 'ừ', 'ử', 'ữ', 'ự', 'Ú', 'Ù', 'Ủ', 'Ũ', 'Ụ', 'Ư', 'Ứ', 'Ừ', 'Ử', 'Ữ', 'Ự' },
            { 'y', 'ý', 'ỳ', 'ỷ', 'ỹ', 'ỵ', 'Ý', 'Ỳ', 'Ỷ', 'Ỹ', 'Ỵ' },
            { 'd', 'đ', 'Đ' }
    };

    static {
        normal = new char[Character.MAX_VALUE];
        for (int i = Character.MIN_VALUE; i < Character.MAX_VALUE; i++) {
            if ('A' <= i && i <= 'Z') {
                normal[i] = Character.toLowerCase((char) i);
            } else {
                normal[i] = (char) i;
            }
        }
        char base;
        for (char[] tone : tones) {
            base = tone[0];
            for (int j = 1; j < tone.length; j++) {
                normal[tone[j]] = base;
            }
        }
    }

    public static String normal(String sentence) {
        if (sentence == null) {
            return null;
        }
        StringBuilder result = new StringBuilder(sentence.length());
        char c;
        for (int i = 0; i < sentence.length(); i++) {
            c = normal[sentence.charAt(i)];
            if (32 <= c && c < 127) {
                result.append(c);
            }
        }
        return result.toString();
    }
}
