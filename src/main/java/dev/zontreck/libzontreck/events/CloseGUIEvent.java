package dev.zontreck.libzontreck.events;

import dev.zontreck.libzontreck.chestgui.ChestGUI;
import dev.zontreck.libzontreck.util.ServerUtilities;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class CloseGUIEvent extends Event
{
    public ChestGUI gui;
    public Player player;

    public CloseGUIEvent(ChestGUI gui, ServerPlayer player)
    {
        this.gui = gui;
        this.player = player;
    }
}
