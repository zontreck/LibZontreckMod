package dev.zontreck.libzontreck.chestgui;

import dev.zontreck.libzontreck.lore.LoreContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

/**
 * This should be used in place of Runnable for ChestGUI
 */
@FunctionalInterface
public interface IChestGUIButtonCallback
{
    /**
     * A callback function that when invoked will pass the ChestGUI ItemStack
     * @param stack A temporary itemstack that is used for the ChestGUI
     * @param container The container object for manipulating other items when this is invoked
     * @param lore The lore's container instance
     */
    void run(ItemStack stack, ItemStackHandler container, LoreContainer lore);
}
