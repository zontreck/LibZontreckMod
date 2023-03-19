package dev.zontreck.libzontreck.util;

import java.time.Instant;
import java.util.Random;

public class BinUtil {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * Converts a byte array to hexadecimal
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * @return A random instance backed by the time including milliseconds as the seed.
     */
    public static Random getARandomInstance()
    {
        return new Random(Instant.now().toEpochMilli());
    }
}
