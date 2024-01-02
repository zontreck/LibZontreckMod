package dev.zontreck.libzontreck.chestgui;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.menus.ChestGUIMenu;
import dev.zontreck.libzontreck.networking.packets.ChestGUIOpenC2S;
import dev.zontreck.libzontreck.util.ServerUtilities;
import dev.zontreck.libzontreck.vectors.Vector2;
import dev.zontreck.libzontreck.vectors.Vector2i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChestGUI
{
    private ItemStackHandler container = new ItemStackHandler((9*3));
    private String MenuTitle;
    private UUID player;
    private List<ChestGUIButton> buttons = new ArrayList<>();
    private ResourceLocation id;
    public ChestGUI withButton(ChestGUIButton button)
    {
        buttons.add(button);
        container.setStackInSlot(button.getSlotNum(), button.buildIcon());

        return this;
    }

    public ChestGUI withTitle(String title)
    {
        MenuTitle = title;

        return this;
    }

    public static ChestGUI builder()
    {
        return new ChestGUI();
    }

    public ChestGUI withPlayer(UUID id)
    {
        player=id;
        return this;
    }

    /**
     * Open the GUI on the client
     */
    public void open()
    {
        if(LibZontreck.CURRENT_SIDE == LogicalSide.SERVER)
        {
            NetworkHooks.openScreen(ServerUtilities.getPlayerByID(player.toString()), new SimpleMenuProvider(ChestGUIMenu.getServerMenu(this), Component.literal(MenuTitle)));
        }
    }

    public boolean isPlayer(UUID ID)
    {
        return player.equals(ID);
    }

    public ChestGUI withGUIId(ResourceLocation id)
    {
        this.id = id;
        return this;
    }

    public boolean matches(ResourceLocation id)
    {
        return this.id.equals(id);
    }

    public void handleButtonClicked(int slot, Vector2 pos, Item item) {
        for(ChestGUIButton button : buttons)
        {
            if(button.getSlotNum() == slot)
            {
                if(button.buildIcon().getItem() == item)
                {
                    button.clicked();
                    return;
                }
            }
        }
    }

    public boolean hasSlot(Vector2i slot)
    {
        for(ChestGUIButton btn : buttons)
        {
            if(btn.matchesSlot(slot))
            {
                return true;
            }
        }

        return false;
    }

    public ChestGUIButton getSlot(Vector2i slot) {
        if(hasSlot(slot))
        {
            for(ChestGUIButton btn : buttons)
            {
                if(btn.matchesSlot(slot))
                {
                    return btn;
                }
            }
        }
        return null;
    }
}
