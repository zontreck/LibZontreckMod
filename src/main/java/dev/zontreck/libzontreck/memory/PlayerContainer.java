package dev.zontreck.libzontreck.memory;

import java.util.UUID;

import dev.zontreck.libzontreck.LibZontreck;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class PlayerContainer {
    public UUID ID;
    public PlayerComponent player;
    public CompoundTag miscData;

    public PlayerContainer(UUID ID)
    {
        this(LibZontreck.THE_SERVER.getPlayerList().getPlayer(ID));
    }
    public PlayerContainer(ServerPlayer player)
    {
        this.player = new PlayerComponent(player);
        miscData=new CompoundTag();
        ID = player.getUUID();
    }
}
