package dev.zontreck.libzontreck.events;

import dev.zontreck.libzontreck.util.ServerUtilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.util.UUID;

@Cancelable
public class OpenGUIEvent extends Event
{
    private  ResourceLocation GUIId;
    private UUID playerID;

    public OpenGUIEvent(ResourceLocation ID, UUID player)
    {
        GUIId = ID;
        playerID = player;
    }

    public boolean matches(ResourceLocation id)
    {
        return GUIId.equals(id);
    }

    public ServerPlayer getPlayer()
    {
        return ServerUtilities.getPlayerByID(getPlayer().getStringUUID());
    }
}
