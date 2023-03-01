package dev.zontreck.libzontreck.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

import com.google.gson.Gson;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.util.HeadCache.HeadCacheItem;
import net.minecraft.world.item.ItemStack;

/**
 * Added to showcase Patreon supporters and those who have helped test or provide feedback and suggestions!
 */
public class HeadUtilities {
    private static HeadCacheItem cachedLookup(String playerName)
    {
        if(HeadCache.CACHE.hasHead(playerName))
        {
            HeadCacheItem item = HeadCache.CACHE.getHead(playerName);
            return item;
        }else {
            // Look up head then add to cache
            return externalHeadRequest(playerName);
        }
    }

    public static ItemStack get(String playerName)
    {
        return cachedLookup(playerName).getAsItem();
    }

    private static HeadCacheItem externalHeadRequest(String playerName)
    {
        String data="";
        try {
            data = HttpHelper.getFrom(new URL(LibZontreck.PLAYER_INFO_URL + playerName));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if(data.equals("")){
            return null;
        }

        UUID PlayerID = null;
        String playerTexture="";
        try{
            Gson gson = new Gson();
            PlayerInfo info = gson.fromJson(data, PlayerInfo.class);

            String data2 = HttpHelper.getFrom(new URL(LibZontreck.PLAYER_SKIN_URL + info.id));

            PlayerProfileInfo info2 = gson.fromJson(data2, PlayerProfileInfo.class);
            playerTexture = info2.properties.get(0).value;
            PlayerID = HeadCache.HeadCacheItem.toNewID(info.id);
            

            return HeadCache.CACHE.addToCache(PlayerID, playerTexture, playerName);
        }catch(Exception e)
        {
            return null;
        }
    }
}
