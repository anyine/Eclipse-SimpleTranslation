package com.wuwenze.eclipse.simpletranslation.util;

public final class StringUtil {

    public static boolean isEmpty(String text) {
        return null == text || "".equals(text) || "null".equalsIgnoreCase(text);
    }
}
