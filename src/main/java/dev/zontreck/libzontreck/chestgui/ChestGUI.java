package dev.zontreck.libzontreck.chestgui;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.events.CloseGUIEvent;
import dev.zontreck.libzontreck.events.OpenGUIEvent;
import dev.zontreck.libzontreck.items.ModItems;
import dev.zontreck.libzontreck.menus.ChestGUIMenu;
import dev.zontreck.libzontreck.networking.ModMessages;
import dev.zontreck.libzontreck.networking.packets.S2CCloseChestGUI;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.util.ServerUtilities;
import dev.zontreck.libzontreck.vectors.Vector2;
import dev.zontreck.libzontreck.vectors.Vector2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * Zontreck's ChestGUI Interface
 * <p>
 * This was heavily inspired by some of the ChestGUI's seen in Spigot mods.
 * The reason for creating this system is to rapidly prototype interfaces. This is meant to be a helper to add a GUI quickly and easily without all the mess and fuss of making a menu or a screen. This is meant to be a stepping stone, not a permanent replacement to a proper UI.
 * <p>
 * This implementation is unlikely to ever change much, as it is just meant to accomplish the above task, and it does, successfully.
 */
public class ChestGUI
{
    private ItemStackHandler container = new ItemStackHandler((9*3));
    private String MenuTitle = "";
    private UUID player;
    public List<ChestGUIButton> buttons = new ArrayList<>();
    private ChestGUIIdentifier id;
    private int page =0;
    public boolean hasAdd = false;
    public boolean hasReset = false;
    public boolean hasRemove = false;

    private IChestGUIButtonCallback onAdd;
    private IChestGUIButtonCallback onReset;
    private IChestGUIButtonCallback onRemove;

    public ChestGUIButton addBtn = null;
    public ChestGUIButton resetBtn = null;
    public ChestGUIButton removeBtn = null;



    public ChestGUI withAdd(IChestGUIButtonCallback onAdd)
    {
        hasAdd=true;
        this.onAdd=onAdd;
        return this;
    }

    public ChestGUI withReset(IChestGUIButtonCallback onReset)
    {
        hasReset = true;
        this.onReset = onReset;
        return this;
    }

    private ChestGUI withRemove(IChestGUIButtonCallback onRemove)
    {
        hasRemove = true;
        this.onRemove=onRemove;
        return this;
    }
    public ChestGUI withButton(ChestGUIButton button)
    {
        if(buttons.size()>=2*9)
        {
            return this;
        }
        buttons.add(button);
        button.withContainer(container);
        container.setStackInSlot(button.getSlotNum(), button.buildIcon());

        return this;
    }


        /*
        X   X   X   X   X   X   X   X   X
        X   X   X   X   X   X   X   X   X
        0   0   0   -   @   +   0   0   0
         */

    // LEGEND:
    // X = ChestGUIButton
    // 0 = Empty Slot
    // - = Remove / Subtract
    // @ = Reset / Refresh
    // + = Add

    /**
     * Updates the menu's utility buttons
     */
    public void updateUtilityButtons()
    {

        if(hasRemove)
        {
            ItemStack remStack = new ItemStack(ModItems.CHESTGUI_REM.get(), 1);

            ChestGUIButton rem = new ChestGUIButton(remStack, onRemove, new Vector2i(2, 3));
            rem.withContainer(container);

            removeBtn = rem;

            container.setStackInSlot(rem.getSlotNum(), rem.buildIcon());
        }

        if(hasReset)
        {
            ItemStack resStack = new ItemStack(ModItems.CHESTGUI_RESET.get(), 1);

            ChestGUIButton rem = new ChestGUIButton(resStack, onReset, new Vector2i(2, 4));
            rem.withContainer(container);

            resetBtn = rem;

            container.setStackInSlot(rem.getSlotNum(), rem.buildIcon());

        }

        if(hasAdd)
        {

            ItemStack remStack = new ItemStack(ModItems.CHESTGUI_ADD.get(), 1);

            ChestGUIButton rem = new ChestGUIButton(remStack, onAdd, new Vector2i(2, 5));
            rem.withContainer(container);

            addBtn = rem;

            container.setStackInSlot(rem.getSlotNum(), rem.buildIcon());
        }
    }


    public boolean isFirstPage() {
        return page == 0;
    }

    public boolean isLastPage() {
        int maxPerPage = 2 * 9;
        int totalButtons = buttons.size();
        int totalPages = (totalButtons - 1) / maxPerPage;

        return page >= totalPages;
    }


    /**
     * Checks if the number of buttons warrants adding the next/previous buttons to the utility row
     * @return True if the number of buttons exceeds (2*9)
     */
    public boolean hasMultiPage()
    {
        return (buttons.size() > (2*9));
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
            updateUtilityButtons();
            MinecraftForge.EVENT_BUS.post(new OpenGUIEvent(id, player, this));
            NetworkHooks.openScreen(ServerUtilities.getPlayerByID(player.toString()), new SimpleMenuProvider(ChestGUIMenu.getServerMenu(this), ChatHelpers.macro(((MenuTitle != "") ? MenuTitle : "No Title"))));
        }
    }

    /**
     * Send a signal to trigger the GUI to close on the server, then send a signal to the client to also close the interface
     */
    public void close()
    {
        if(LibZontreck.CURRENT_SIDE == LogicalSide.SERVER)
        {
            MinecraftForge.EVENT_BUS.post(new CloseGUIEvent(this, ServerUtilities.getPlayerByID(player.toString())));
            
            ModMessages.sendToPlayer(new S2CCloseChestGUI(), ServerUtilities.getPlayerByID(player.toString()));
        }
    }

    public boolean isPlayer(UUID ID)
    {
        return player.equals(ID);
    }

    public ChestGUI withGUIId(ChestGUIIdentifier id)
    {
        this.id = id;
        return this;
    }

    public boolean matches(ChestGUIIdentifier id)
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
