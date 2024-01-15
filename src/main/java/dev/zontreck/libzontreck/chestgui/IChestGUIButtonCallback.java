package dev.zontreck.libzontreck.chestgui;

import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface IChestGUIButtonCallback
{
    void run(ItemStack stack);
}
