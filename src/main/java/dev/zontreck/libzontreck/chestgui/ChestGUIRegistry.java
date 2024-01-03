package dev.zontreck.libzontreck.chestgui;

import dev.zontreck.libzontreck.events.CloseGUIEvent;
import dev.zontreck.libzontreck.events.OpenGUIEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChestGUIRegistry
{
    private static Map<UUID, ChestGUI> GUIS = new HashMap<>();


    @SubscribeEvent
    public static void onGuiOpen(final OpenGUIEvent event)
    {
        GUIS.put(event.getPlayer().getUUID(), event.getGui());
    }

    @SubscribeEvent
    public static void onGuiClose(final CloseGUIEvent event)
    {
        GUIS.remove(event.player.getUUID());
    }

    public static ChestGUI get(UUID ID)
    {
        if(GUIS.containsKey(ID)) return GUIS.get(ID);
        return null;
    }
}
