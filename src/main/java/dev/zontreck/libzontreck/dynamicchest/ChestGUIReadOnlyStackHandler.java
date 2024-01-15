package dev.zontreck.libzontreck.dynamicchest;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.chestgui.ChestGUI;
import dev.zontreck.libzontreck.chestgui.ChestGUIButton;
import dev.zontreck.libzontreck.chestgui.ChestGUIRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public class ChestGUIReadOnlyStackHandler extends ItemStackHandler
{
    private ChestGUI gui;
    private Player player;
    private long lastClickTime;
    private int lastClickStack=-1;

    public boolean validClick(int slot)
    {
        if(lastClickStack != slot)return true;
        else{
            if(Instant.now().getEpochSecond() > (lastClickTime + 3))
            {
                return true;
            }
        }

        return false;
    }

    public ChestGUIReadOnlyStackHandler(ChestGUI gui, Player player)
    {
        super((3*9));
        this.gui = gui;
        this.player = player;


        LibZontreck.LOGGER.info("Logical Side : " + LibZontreck.CURRENT_SIDE);

        if(gui!=null)
        {
            if(gui.buttons!=null)
            {
                LibZontreck.LOGGER.info("Generating chest gui button items");
                for(ChestGUIButton btn : gui.buttons)
                {
                    setStackInSlot(btn.getSlotNum(), btn.buildIcon());
                }

                if(gui.hasAdd)
                {
                    setStackInSlot(gui.addBtn.getSlotNum(), gui.addBtn.buildIcon());
                }

                if(gui.hasReset)
                {
                    setStackInSlot(gui.resetBtn.getSlotNum(), gui.resetBtn.buildIcon());
                }

                if(gui.hasRemove)
                {
                    setStackInSlot(gui.removeBtn.getSlotNum(), gui.removeBtn.buildIcon());
                }


            } else LibZontreck.LOGGER.error("Gui Buttons list is null");
        } else LibZontreck.LOGGER.error("Gui is null!");
    }

    @Override
    public int getSlots() {
        return (3*9);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        ChestGUI instance = ChestGUIRegistry.get(player.getUUID());
        if(instance==null)return ItemStack.EMPTY;


        ChestGUIButton btn = instance.buttons.stream().filter(x->x.getSlotNum()==slot).findFirst().orElse(null);

        if(btn == null) return ItemStack.EMPTY;

        if(validClick(slot))
        {
            btn.clicked();

            lastClickTime = Instant.now().getEpochSecond();
            lastClickStack = slot;
        }

        return ItemStack.EMPTY;
    }


}
