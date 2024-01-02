package dev.zontreck.libzontreck.dynamicchest;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.chestgui.ChestGUI;
import dev.zontreck.libzontreck.chestgui.ChestGUIButton;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class ChestGUIReadOnlyStackHandler extends ItemStackHandler
{
    private ChestGUI gui;

    public ChestGUIReadOnlyStackHandler(ChestGUI gui)
    {
        super((3*9));
        this.gui = gui;

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
        ChestGUIButton btn = gui.buttons.stream().filter(x->x.getSlotNum()==slot).findFirst().orElse(null);

        if(btn == null) return ItemStack.EMPTY;

        btn.clicked();

        return ItemStack.EMPTY;
    }
}
