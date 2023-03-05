package dev.zontreck.libzontreck.util;

import java.util.UUID;

import dev.zontreck.libzontreck.LibZontreck;
import net.minecraft.server.level.ServerPlayer;

public class ServerUtilities
{
    public static ServerPlayer getPlayerByID(String id)
    {
        return LibZontreck.THE_SERVER.getPlayerList().getPlayer(UUID.fromString(id));
    }
}