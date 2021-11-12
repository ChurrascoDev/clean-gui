package com.github.imthenico.cleangui.util;

import java.util.Objects;

public class Validate {

    public static void isTrue(boolean b) {
        if (!b)
            throw new IllegalArgumentException();
    }

    public static void isTrue(boolean b, String message) {
        if (!b)
            throw new IllegalArgumentException(message);
    }

    public static <T> T isTrue(boolean b, String message, T returnValue) {
        if (!b)
            throw new IllegalArgumentException(message);

        return returnValue;
    }

    public static <T> T notNull(T obj) {
        return Objects.requireNonNull(obj);
    }

    public static <T> T notNull(T obj, String message) {
        return Objects.requireNonNull(obj, message);
    }

    public static <T> T defIfNull(T obj, T def) {
        if (obj == null)
            return def;

        return obj;
    }
}