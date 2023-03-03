package dev.zontreck.libzontreck.dynamicchest;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.ItemStackHandler;

public class ReadOnlyItemStackHandler extends ItemStackHandler
{
    private final ItemStackHandler slot;
    private Runnable onClick;
    protected ReadOnlyItemStackHandler(ItemStackHandler item)
    {
        super();
        slot=item;
    }

    public ReadOnlyItemStackHandler(ItemStackHandler item, Runnable onClick)
    {
        this(item);
        this.onClick=onClick;
    }

    @Override
    public void setSize(int size)
    {
        stacks = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);
    }

	@Override
	public void setStackInSlot(int num, ItemStack stack) {
		slot.setStackInSlot(num, stack);
	}

	@Override
	public int getSlots() {
		return slot.getSlots();
	}

	@Override
	public ItemStack getStackInSlot(int num) {
		return slot.getStackInSlot(num);
	}


	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		return stack;
	}

	@Override
	public ItemStack extractItem(int num, int amount, boolean simulate) {
        if(onClick != null){
            onClick.run();
            return ItemStack.EMPTY;
        }

		return ItemStack.EMPTY;
	}
}
