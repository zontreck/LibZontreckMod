package dev.zontreck.libzontreck.vectors;

import net.minecraft.nbt.CompoundTag;

public class Points {
    public Vector3 Point1 = Vector3.ZERO;
    public Vector3 Point2 = Vector3.ZERO;


    public Points(Vector3 min, Vector3 max)
    {
        if(min.less(max))
        {
            Point1=min;
            Point2=max;
        }else{
            Point1=max;
            Point2=min;
        }
    }

    public Points(CompoundTag tag){
        deserialize(tag);
    }

    public CompoundTag serialize(){
        CompoundTag tag = new CompoundTag();
        tag.put("min", Point1.serialize());
        tag.put("max", Point2.serialize());
        return tag;
    }

    public void deserialize(CompoundTag tag)
    {
        Point1 = new Vector3(tag.getCompound("min"));
        Point2 = new Vector3(tag.getCompound("max"));
    }
}
