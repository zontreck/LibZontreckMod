package dev.zontreck.libzontreck.networking.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class OpenGUIRequest {
    public ResourceLocation ID;
    public UUID playerID;

    public OpenGUIRequest(CompoundTag tag)
    {
        CompoundTag tags = tag.getCompound("id");

        ID = new ResourceLocation(tags.getString("mod"), tags.getString("id"));
        playerID = tag.getUUID("player");
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("player", playerID);
        CompoundTag tags = new CompoundTag();
        tags.putString("mod", ID.getNamespace());
        tags.putString("id", ID.getPath());

        tag.put("id", tags);

        return tag;
    }

    public OpenGUIRequest(){}
}
