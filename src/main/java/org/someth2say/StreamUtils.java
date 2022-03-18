package org.someth2say;

public class StreamUtils {
    public static String convertStreamToString(final java.io.InputStream is) {
        try (java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A")) {
            return s.hasNext() ? s.next() : "";
        }
    }
}
