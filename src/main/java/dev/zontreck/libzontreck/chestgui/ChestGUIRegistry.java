package dev.zontreck.libzontreck.chestgui;

import dev.zontreck.libzontreck.events.GUIButtonClickedEvent;
import dev.zontreck.libzontreck.vectors.Vector2;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ChestGUIRegistry
{
    public static List<ChestGUI> ActiveGUIs = new ArrayList<>();

    @SubscribeEvent
    public void onChestGUIButtonClicked(GUIButtonClickedEvent event)
    {
        for(ChestGUI gui : ActiveGUIs)
        {
            if(gui.isPlayer(event.player))
            {
                if(gui.matches(event.id))
                {
                    // Handle the click now
                    CompoundTag tag = event.stack.getTag();
                    Vector2 pos = new Vector2(tag.getCompound("pos"));
                    int slot = tag.getInt("slot");
                    gui.handleButtonClicked(slot, pos, event.stack.getItem());
                }

            }
        }
    }
}
