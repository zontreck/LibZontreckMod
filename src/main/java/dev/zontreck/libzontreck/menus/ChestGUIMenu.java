package dev.zontreck.libzontreck.menus;

import dev.zontreck.libzontreck.chestgui.ChestGUI;
import dev.zontreck.libzontreck.chestgui.ChestGUIButton;
import dev.zontreck.libzontreck.types.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class ChestGUIMenu extends AbstractContainerMenu
{
    private ChestGUI gui;

    public ChestGUIMenu(int id, Inventory playerInv, FriendlyByteBuf buf)
    {
        this(id, playerInv, new ItemStackHandler(3*9), BlockPos.ZERO, playerInv.player, null);
    }

    public ChestGUIMenu(int id, Inventory playerInv, ItemStackHandler inv, BlockPos position, Player player, ChestGUI gui)
    {
        super(ModMenuTypes.CHEST_GUI_MENU.get(), id);

        this.gui = gui;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public static MenuConstructor getServerMenu(ItemStackHandler inventory)
    {
        return (id, playerInv, player) -> new ChestGUIMenu(id, playerInv, inventory, BlockPos.ZERO, player, null);
    }
}
