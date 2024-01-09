package dev.zontreck.libzontreck.chestgui;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.events.CloseGUIEvent;
import dev.zontreck.libzontreck.events.OpenGUIEvent;
import dev.zontreck.libzontreck.items.ModItems;
import dev.zontreck.libzontreck.menus.ChestGUIMenu;
import dev.zontreck.libzontreck.networking.ModMessages;
import dev.zontreck.libzontreck.networking.packets.S2CCloseChestGUI;
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

public class ChestGUI
{
    private ItemStackHandler container = new ItemStackHandler((9*3));
    private String MenuTitle = "";
    private UUID player;
    public List<ChestGUIButton> buttons = new ArrayList<>();
    private ResourceLocation id;
    private int page =0;
    private boolean hasAdd = false;
    private boolean hasReset = false;
    private boolean hasRemove = false;

    private Runnable onAdd;
    private Runnable onReset;
    private Runnable onRemove;


    public ChestGUI withAdd(Runnable onAdd)
    {
        hasAdd=true;
        this.onAdd=onAdd;
        return this;
    }

    public ChestGUI withReset(Runnable onReset)
    {
        hasReset = true;
        this.onReset = onReset;
        return this;
    }

    private ChestGUI withRemove(Runnable onRemove)
    {
        hasRemove = true;
        this.onRemove=onRemove;
        return this;
    }
    public ChestGUI withButton(ChestGUIButton button)
    {
        buttons.add(button);
        //container.setStackInSlot(button.getSlotNum(), button.buildIcon());

        return this;
    }

    /**
     * Increment to the next page
     */
    public void nextPage()
    {
        page++;
    }

    /**
     * Go back a previous page, if possible
     */
    public void prevPage()
    {
        page--;
    }

        /*
        X   X   X   X   X   X   X   X   X
        X   X   X   X   X   X   X   X   X
        <   0   0   -   @   +   0   0   >
         */

    // LEGEND:
    // X = ChestGUIButton
    // < = Previous Page Button
    // 0 = Empty Slot
    // - = Remove / Subtract
    // @ = Reset / Refresh
    // + = Add
    // > = Next Page

    /**
     * Sanity checks the page update
     */
    public void checkPageButtons() {
        int maxPerPage = 2 * 9;
        int maxForPage = maxPerPage * page;

        int totalButtons = buttons.size();
        int totalPages = (totalButtons - 1) / maxPerPage; // Calculate total pages

        // Ensure the current page is within bounds
        if (page < 0) {
            page = 0;
        } else if (page > totalPages) {
            page = totalPages;
        }

        // Perform additional logic if needed for displaying buttons on the GUI
        // ...

        updateContainerForPage(); // Update the container for the current page
    }

    /**
     * Update the container with the page's buttons
     */
    public void updateContainerForPage() {
        int maxPerPage = 2 * 9;
        int startIndex = maxPerPage * page;
        int endIndex = Math.min(startIndex + maxPerPage, buttons.size());

        // Logic to update the container based on buttons for the current page
        ItemStackHandler pageContainer = new ItemStackHandler((9 * 3)); // Create a new container for the page

        for (int i = startIndex; i < endIndex; i++) {
            ChestGUIButton button = buttons.get(i);

            // Calculate position relative to the page
            int relativeIndex = i - startIndex;
            int row = relativeIndex / 9;
            int col = relativeIndex % 9;

            Vector2i position = new Vector2i(row, col); // Create position for the button
            button.withPosition(position); // Set the button's position

            int slot = row * 9 + col; // Calculate the slot based on (row, column)
            pageContainer.setStackInSlot(slot, button.buildIcon()); // Add button to the container
        }

        if(hasMultiPage())
        {
            if(!isFirstPage())
            {
                ItemStack backStack = new ItemStack(ModItems.CHESTGUI_BACK.get(), 1);
                ChestGUIButton prev = new ChestGUIButton(backStack, ()->{
                    close();
                    prevPage();
                    open();
                }, new Vector2i(3, 0));

                pageContainer.setStackInSlot(prev.getSlotNum(), prev.buildIcon());
            }

            if(!isLastPage())
            {

                ItemStack forwardStack = new ItemStack(ModItems.CHESTGUI_FORWARD.get(), 1);
                ChestGUIButton nxt = new ChestGUIButton(forwardStack, ()->{
                    close();
                    nextPage();
                    open();
                }, new Vector2i(3, 8));

                pageContainer.setStackInSlot(nxt.getSlotNum(), nxt.buildIcon());
            }
        }

        if(hasRemove)
        {
            ItemStack remStack = new ItemStack(ModItems.CHESTGUI_REM.get(), 1);

            ChestGUIButton rem = new ChestGUIButton(remStack, ()-> {
                onRemove.run();
            }, new Vector2i(3, 3));

            pageContainer.setStackInSlot(rem.getSlotNum(), rem.buildIcon());
        }

        if(hasReset)
        {
            ItemStack resStack = new ItemStack(ModItems.CHESTGUI_RESET.get(), 1);

            ChestGUIButton rem = new ChestGUIButton(resStack, ()-> {
                onReset.run();
            }, new Vector2i(3, 4));

            pageContainer.setStackInSlot(rem.getSlotNum(), rem.buildIcon());

        }

        if(hasAdd)
        {

            ItemStack remStack = new ItemStack(ModItems.CHESTGUI_ADD.get(), 1);

            ChestGUIButton rem = new ChestGUIButton(remStack, ()-> {
                onAdd.run();
            }, new Vector2i(3, 5));

            pageContainer.setStackInSlot(rem.getSlotNum(), rem.buildIcon());
        }

        this.container = pageContainer; // Update the container with the new page content
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
            MinecraftForge.EVENT_BUS.post(new OpenGUIEvent(id, player, this));
            NetworkHooks.openScreen(ServerUtilities.getPlayerByID(player.toString()), new SimpleMenuProvider(ChestGUIMenu.getServerMenu(this), Component.literal((MenuTitle != "") ? MenuTitle : "No Title")));
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
