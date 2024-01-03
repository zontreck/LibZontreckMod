package dev.zontreck.libzontreck.lore;

import dev.zontreck.libzontreck.chat.ChatColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class LoreContainer {
    private int loreEntryNumber;
    public ExtraLore miscData = new ExtraLore();
    private final ItemStack associatedItem;

    public LoreContainer(ItemStack stack) {
        associatedItem = stack;
        loreEntryNumber = getLoreEntryNumber(stack);
        parseExistingLore(stack);
    }

    public void commitLore() {
        assertLoreExists();
        CompoundTag tag = associatedItem.getOrCreateTag();
        CompoundTag display = tag.getCompound(ItemStack.TAG_DISPLAY);
        ListTag lore = miscData.saveEntries();

        display.put(ItemStack.TAG_LORE, lore);
        tag.put(ItemStack.TAG_DISPLAY, display);
        associatedItem.setTag(tag);
    }

    public void clear() {
        loreEntryNumber = 0;
        miscData.loreData.clear();
        CompoundTag tag = associatedItem.getOrCreateTag().getCompound(ItemStack.TAG_DISPLAY);
        tag.remove(ItemStack.TAG_LORE);
        commitLore();
    }

    private void setOrUpdateIndex(ListTag lst, int pos, Tag insert) {
        if (lst.size() <= pos) {
            lst.add(insert);
            loreEntryNumber = lst.size() - 1;
        } else {
            lst.set(pos, insert);
        }
    }

    private void assertLoreExists() {
        assertTag();
        assertDisplay();
        assertLore();
    }

    private void assertTag() {
        if (!associatedItem.hasTag()) {
            associatedItem.setTag(new CompoundTag());
        }
    }

    private void assertDisplay() {
        CompoundTag tag = associatedItem.getOrCreateTag();
        CompoundTag display = tag.getCompound(ItemStack.TAG_DISPLAY);
        if (display.isEmpty()) {
            tag.put(ItemStack.TAG_DISPLAY, new CompoundTag());
            associatedItem.setTag(tag);
        }
    }

    private void assertLore() {
        CompoundTag tag = associatedItem.getOrCreateTag();
        CompoundTag display = tag.getCompound(ItemStack.TAG_DISPLAY);
        ListTag lore = display.getList(ItemStack.TAG_LORE, Tag.TAG_STRING);
        if (lore.isEmpty()) {
            display.put(ItemStack.TAG_LORE, new ListTag());
            associatedItem.setTag(tag);
        }
    }

    private int getLoreEntryNumber(ItemStack stack) {
        CompoundTag display = stack.getOrCreateTag().getCompound(ItemStack.TAG_DISPLAY);
        ListTag loreEntries = display.getList(ItemStack.TAG_LORE, Tag.TAG_STRING);
        return (loreEntries != null) ? loreEntries.size() : 0;
    }

    private void parseExistingLore(ItemStack stack) {
        CompoundTag display = stack.getOrCreateTag().getCompound(ItemStack.TAG_DISPLAY);
        ListTag loreEntries = display.getList(ItemStack.TAG_LORE, Tag.TAG_STRING);
        miscData = new ExtraLore(loreEntries);
    }
}
