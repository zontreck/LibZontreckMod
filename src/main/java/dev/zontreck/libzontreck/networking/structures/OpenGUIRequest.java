package dev.zontreck.libzontreck.networking.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class OpenGUIRequest {
    public List<ItemStack> options = new ArrayList<>();
    public String GUITitle;
    public UUID playerID;

    public OpenGUIRequest(CompoundTag tag)
    {
        ListTag items = tag.getList("items", Tag.TAG_COMPOUND);

        for(Tag tags : items)
        {
            ItemStack is = ItemStack.of((CompoundTag)tags);
            options.add(is);
        }

        GUITitle = tag.getString("title");
        playerID = tag.getUUID("player");
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.putString("title", GUITitle);
        tag.putUUID("player", playerID);
        ListTag lst = new ListTag();
        for (ItemStack itemStack : options) {
            lst.add(itemStack.serializeNBT());
        }
        tag.put("items", lst);

        return tag;
    }

    public OpenGUIRequest(){}
}
