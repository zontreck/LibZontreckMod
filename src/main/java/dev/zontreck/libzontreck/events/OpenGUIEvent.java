package dev.zontreck.libzontreck.events;

import dev.zontreck.libzontreck.chestgui.ChestGUI;
import dev.zontreck.libzontreck.util.ServerUtilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.util.UUID;

public class OpenGUIEvent extends Event
{
    private  ResourceLocation GUIId;
    private UUID playerID;
    private final ChestGUI gui;

    public OpenGUIEvent(ResourceLocation ID, UUID player, ChestGUI gui)
    {
        GUIId = ID;
        playerID = player;
        this.gui = gui;
    }

    public boolean matches(ResourceLocation id)
    {
        return GUIId.equals(id);
    }

    public ServerPlayer getPlayer()
    {
        return ServerUtilities.getPlayerByID(playerID.toString());
    }

    public ChestGUI getGui() {
        return gui;
    }
}
