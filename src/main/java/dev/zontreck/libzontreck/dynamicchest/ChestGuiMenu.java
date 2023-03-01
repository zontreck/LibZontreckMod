package dev.zontreck.libzontreck.dynamicchest;

import dev.zontreck.libzontreck.networking.structures.OpenGUIRequest;
import dev.zontreck.libzontreck.types.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ChestGuiMenu extends AbstractContainerMenu
{
    public final Player player;
    
    public ChestGuiMenu(int id, Inventory player)
    {
        this(id, player, new ItemStackHandler(36), BlockPos.ZERO, player.player, null);
    }

    public ChestGuiMenu(int id, Inventory player, IItemHandler handler, BlockPos pos, Player play, OpenGUIRequest request)
    {
        super(ModMenuTypes.CHESTGUI.get(), id);
        this.player=play;

        int slotSize=18;
        int startX=11;
        int startY=11;

        for(int row=0; row<4; row++)
        {
            for(int column = 0; column<9;column++)
            {
                addSlot(new SlotItemHandler(handler, row * 9 + column, startX + column * slotSize, startY + row * slotSize));
            }
        }

    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true; // This is dynamic. We have no block entity!
    }

}
