package dev.zontreck.libzontreck.permissions;

import net.minecraft.nbt.CompoundTag;

public class Permission {
    public String permName;
    boolean isGroup(){
        return false;
    }

    public Permission(String name){

        this.permName = name;
    }

    public CompoundTag save()
    {
        CompoundTag tag=new CompoundTag();
        tag.putString("name", permName);

        return tag;
    }

    public Permission(CompoundTag tag)
    {
        this(tag.getString("name"));
    }
}
