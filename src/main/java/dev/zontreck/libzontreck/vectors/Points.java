package dev.zontreck.libzontreck.vectors;

import net.minecraft.nbt.CompoundTag;

public class Points {
    public Vector3 Min = Vector3.ZERO;
    public Vector3 Max = Vector3.ZERO;


    public Points(Vector3 min, Vector3 max)
    {
        if(min.less(max))
        {
            Min=min;
            Max=max;
        }else{
            Min=max;
            Max=min;
        }
    }

    public Points(CompoundTag tag){
        deserialize(tag);
    }

    public CompoundTag serialize(){
        CompoundTag tag = new CompoundTag();
        tag.put("min", Min.serialize());
        tag.put("max", Max.serialize());
        return tag;
    }

    public void deserialize(CompoundTag tag)
    {
        Min = new Vector3(tag.getCompound("min"));
        Max = new Vector3(tag.getCompound("max"));
    }
}
