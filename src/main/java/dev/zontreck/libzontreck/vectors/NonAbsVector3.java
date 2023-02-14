package dev.zontreck.libzontreck.vectors;

import net.minecraft.nbt.CompoundTag;

/*
* This is a non-serializable instanced Vector that is meant to slam positions down as a integer
*/
public class NonAbsVector3
{
    public long x;
    public long y;
    public long z;

    public NonAbsVector3(Vector3 origin)
    {
        x = Math.round(origin.x);
        y = Math.round(origin.y);
        z = Math.round(origin.z);
    }

    
    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.putLong("x", x);
        tag.putLong("y", y);
        tag.putLong("z", z);

        return tag;
    }

    public NonAbsVector3(CompoundTag tag) {
        this.deserialize(tag);
    }
    public void deserialize(CompoundTag tag)
    {
        x=tag.getLong("x");
        y=tag.getLong("y");
        z=tag.getLong("z");
    }

    
    public boolean same(NonAbsVector3 other)
    {
        if(x == other.x && y==other.y && z==other.z)return true;
        else return false;
    }
    
    public boolean inside(NonAbsVector3 point1, NonAbsVector3 point2)
    {
        if(point1.x <= x && point2.x >= x){
            if(point1.y <= y && point2.y >= y)
            {
                if(point1.z <= z && point2.z >= z)
                {
                    return true;
                }
            }
        }

        return false;
    }
}
