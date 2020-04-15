package org.dvare.util;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class TrimString {
    public static String trim(String value) {
        if (value != null && !value.isEmpty()) {
            char[] chars = value.toCharArray();
            if (chars.length >= 2 && chars[0] == '\'' && chars[chars.length - 1] == '\'') {
                return value.substring(1, chars.length - 1);
            }
        }
        return value;
    }
}
