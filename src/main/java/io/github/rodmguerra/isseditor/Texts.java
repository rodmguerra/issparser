package io.github.rodmguerra.isseditor;

import java.util.Locale;
import java.util.ResourceBundle;

public class Texts {
    public static String string(String... parts) {
        String key = String.join(".", parts);
        ResourceBundle bundle = ResourceBundle.getBundle("texts");
        return bundle.getString(key).trim();
    }

    public static String[] strings(String... parts) {
        return string(parts).split("\\s*;\\s*");
    }
}