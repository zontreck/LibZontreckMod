package dev.zontreck.libzontreck.vectors;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

/**
 * Two points within the same dimension
 */
public class Points {
    public Vector3 Min = Vector3.ZERO;
    public Vector3 Max = Vector3.ZERO;
    public String dimension = "";

    /**
     * Creates a new set of points
     * @param min
     * @param max
     * @param lvl
     */
    public Points(Vector3 min, Vector3 max, ServerLevel lvl)
    {
        dimension = WorldPosition.getDimSafe(lvl);
        if(min.less(max))
        {
            Min=min;
            Max=max;
        }else{
            Min=max;
            Max=min;
        }
    }

    /**
     * Deserializes a points compound tag
     * @param tag
     */
    public Points(CompoundTag tag){
        deserialize(tag);
    }

    public CompoundTag serialize(){
        CompoundTag tag = new CompoundTag();
        tag.put("min", Min.serialize());
        tag.put("max", Max.serialize());
        tag.putString("dim", dimension);
        return tag;
    }

    public void deserialize(CompoundTag tag)
    {
        Min = new Vector3(tag.getCompound("min"));
        Max = new Vector3(tag.getCompound("max"));
        dimension = tag.getString("dim");
    }
}
