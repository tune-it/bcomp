package ru.ifmo.cs.components;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class Messages {

    private static final String BUNDLE_NAME = "ru.ifmo.cs.components.MessagesBundle";

    private static volatile ResourceBundle bundle =
            ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());

    private Messages() {}

    // Falls back to the key itself on miss so partially localized code still runs.
    public static String get(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public static String format(String key, Object... args) {
        return MessageFormat.format(get(key), args);
    }

    public static void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
    }
}
