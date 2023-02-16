package dev.zontreck.libzontreck.memory;

import java.util.UUID;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.exceptions.InvalidDeserialization;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class PlayerComponent
{
    public ServerPlayer player;
    public WorldPosition position;
    public WorldPosition lastPosition;

    public PlayerComponent(ServerPlayer play)
    {
        player=play;
        position = new WorldPosition(play);
    }

    public boolean positionChanged()
    {
        WorldPosition wp = new WorldPosition(player);
        return !(wp.same(position));
    }

    public void update()
    {
        lastPosition=position;
        position = new WorldPosition(player);
    }

    public static PlayerComponent fromID(UUID ID)
    {
        return new PlayerComponent(LibZontreck.THE_SERVER.getPlayerList().getPlayer(ID));
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("id", player.getUUID());
        tag.put("pos", position.serialize());
        return tag;
    }

    public static PlayerComponent deserialize(CompoundTag tag)
    {
        PlayerComponent comp = PlayerComponent.fromID(tag.getUUID("id"));
        try {
            comp.position = new WorldPosition(tag.getCompound("pos"), false);
        } catch (InvalidDeserialization e) {
            e.printStackTrace();
        }


        return comp;
    }
}
