package dev.zontreck.libzontreck.menus;

import dev.zontreck.libzontreck.chestgui.ChestGUI;
import dev.zontreck.libzontreck.chestgui.ChestGUIButton;
import dev.zontreck.libzontreck.dynamicchest.ReadOnlyItemStackHandler;
import dev.zontreck.libzontreck.types.ModMenuTypes;
import dev.zontreck.libzontreck.vectors.Vector2i;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class ChestGUIMenu extends AbstractContainerMenu
{
    public final ChestGUI gui;

    public ChestGUIMenu(int id, Inventory playerInv, FriendlyByteBuf buf)
    {
        this(id, playerInv, BlockPos.ZERO, playerInv.player, null);
    }

    public ChestGUIMenu(int id, Inventory playerInv, BlockPos position, Player player, ChestGUI gui)
    {
        super(ModMenuTypes.CHEST_GUI_MENU.get(), id);

        this.gui = gui;

        if(gui == null)return;

        int slotSize = 18;
        int startX = 15;
        int startY = 15;

        for (int row = 0; row < 3; row++)
        {
            for(int column=0;column<9;column++)
            {
                Vector2i slot = new Vector2i(row, column);
                ChestGUIButton btn = gui.getSlot(slot);
                if(gui.hasSlot(slot))
                {
                    addSlot(new SlotItemHandler(new ReadOnlyItemStackHandler(btn.buildIconStack(), btn::clicked), row*9 + column, startX + column * slotSize, startY + row * slotSize));
                }
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public static MenuConstructor getServerMenu(ChestGUI gui)
    {
        return (id, playerInv, player) -> new ChestGUIMenu(id, playerInv, BlockPos.ZERO, player, gui);
    }
}
