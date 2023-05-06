package dev.zontreck.libzontreck.util;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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

    public static byte[] hexToBytes(String hexStr)
    {
        List<Byte> bList = new ArrayList<>();
        for(int i=0;i<hexStr.length();i+=2)
        {
            bList.add((byte)(
                    (Character.digit(hexStr.charAt(i), 16)<<4) +
                            (Character.digit(hexStr.charAt(i+1), 16))
                    ));
        }

        return byteArray(bList);
    }

    public static byte[] byteArray(List<Byte> b)
    {
        byte[] ret = new byte[b.size()];
        int i=0;
        for(byte bx : b)
        {
            ret[i] = bx;
            i++;
        }
        return ret;
    }

    /**
     * @return A random instance backed by the time including milliseconds as the seed.
     */
    public static Random getARandomInstance()
    {
        return new Random(Instant.now().toEpochMilli());
    }
}
