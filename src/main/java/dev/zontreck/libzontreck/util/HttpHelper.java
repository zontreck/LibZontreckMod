package dev.zontreck.libzontreck.util;

import java.net.URL;
import java.util.Scanner;

public class HttpHelper {
    public static String getFrom(URL url)
    {
        String data = "";
        try (Scanner s = new Scanner(url.openStream()))
        {
            s.useDelimiter("\\A");
            data = s.hasNext() ? s.next() : "";
        }catch(Exception e)
        {

        }


        return data;
    }
}
