package dev.zontreck.libzontreck.items.lore;

import dev.zontreck.libzontreck.chat.ChatColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class LoreContainer {
    public int loreEntryNumber;
    public ExtraLore miscData;

    private ItemStack associatedItem;


    public LoreContainer(CompoundTag container, ItemStack associated)
    {
        loreEntryNumber = container.getInt("pos");
        miscData = new ExtraLore(container.getCompound("state"));

        this.associatedItem = associated;
    }

    public LoreContainer(ItemStack stack)
    {
        this.associatedItem=stack;
        // Set the loreentrynumber appropriately, and insert a blank entry to hold it's position
        CompoundTag display = stack.getOrCreateTag().getCompound(ItemStack.TAG_DISPLAY);
        ListTag loreEntries = null;
        if(display!= null)
        {
            loreEntries = display.getList(ItemStack.TAG_LORE, Tag.TAG_COMPOUND);
            if(loreEntries==null)
            {
                loreEntryNumber=0;
            }else {
                loreEntryNumber = loreEntries.size(); // This will be the next position
            }
        }else {
            loreEntryNumber=0;
        }

        miscData = new ExtraLore();
        LoreEntry blank = new LoreEntry();
        blank.text = ChatColor.WHITE + "Nothing to see here";
        
        miscData.LoreData.add(blank);

        commitLore();
        
    }

    public void commitLore()
    {
        AssertLoreExists();

        // Set the Lore
        CompoundTag tag = associatedItem.getTag();
        CompoundTag display = tag.getCompound(ItemStack.TAG_DISPLAY);
        ListTag lore = display.getList(ItemStack.TAG_LORE, Tag.TAG_STRING);

        // Set the lore entry
        SetOrUpdateIndex(lore, loreEntryNumber, StringTag.valueOf(miscData.saveJson()));
        display.put(ItemStack.TAG_LORE, lore);
        tag.put(ItemStack.TAG_DISPLAY, display);
        associatedItem.setTag(tag);

        
    }

    private void SetOrUpdateIndex(ListTag lst, int pos, Tag insert)
    {
        if(lst.size() <= pos){
            lst.add(insert);
            // Update the loreEntryNumber
            loreEntryNumber = lst.indexOf(insert);
        }else lst.set(pos, insert);
    }

    private void AssertLoreExists()
    {
        AssertTag();
        AssertDisplay();
        AssertLore();
    }

    private void AssertTag()
    {
        if(!associatedItem.hasTag()){
            associatedItem.setTag(new CompoundTag());
        }
    }

    private void AssertDisplay()
    {
        CompoundTag tag = associatedItem.getTag();
        CompoundTag display = tag.getCompound(ItemStack.TAG_DISPLAY);
        if(display==null)
        {
            tag.put(ItemStack.TAG_DISPLAY, new CompoundTag());
            associatedItem.setTag(tag);
        }
    }

    private void AssertLore()
    {
        CompoundTag tag = associatedItem.getTag();
        CompoundTag display = tag.getCompound(ItemStack.TAG_DISPLAY);
        ListTag lore = display.getList(ItemStack.TAG_LORE, Tag.TAG_STRING);

        if(lore == null)
        {
            lore = new ListTag();
            display.put(ItemStack.TAG_LORE, lore);
            tag.put(ItemStack.TAG_DISPLAY, display);
            associatedItem.setTag(tag);
        }
    }
}
