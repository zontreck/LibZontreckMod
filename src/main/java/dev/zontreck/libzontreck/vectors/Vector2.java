package dev.zontreck.libzontreck.vectors;

import dev.zontreck.libzontreck.exceptions.InvalidDeserialization;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class Vector2
{
    public float x;
    public float y;
    
    public Vec2 asMinecraftVector(){
        return new Vec2(x, y);
    }

    public Vector2()
    {

    }

    public Vector2(float x, float y)
    {
        this.x=x;
        this.y=y;
    }

    public Vector2(Vec2 pos)
    {
        x=pos.x;
        y=pos.y;
    }

    public Vector2(String pos) throws InvalidDeserialization
    {
        // This will be serialized most likely from the ToString method
        // Parse
        if(pos.startsWith("<"))
        {
            pos=pos.substring(1, pos.length()-1); // Rip off the ending bracket too
            String[] positions = pos.split(", ");
            if(positions.length!=2)
            {
                positions = pos.split(",");
            }

            if(positions.length!=2)
            {
                throw new InvalidDeserialization("Positions must be in the same format provided by ToString() (ex. <1,1> or <1, 1>");
            }

            this.x = Float.parseFloat(positions[0]);
            this.y = Float.parseFloat(positions[1]);
            // We are done now
        }
    }

    public Vector2 Clone()
    {
        Vector2 n = new Vector2(x, y);
        return n;
    }

    @Override
    public String toString()
    {
        return "<"+String.valueOf(x)+", "+String.valueOf(y) + ">";
    }

    
    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("x", x);
        tag.putFloat("y", y);

        return tag;
    }

    public Vector2(CompoundTag tag) {
        this.deserialize(tag);
    }
    public void deserialize(CompoundTag tag)
    {
        x=tag.getFloat("x");
        y=tag.getFloat("y");
    }

    public boolean same(Vector2 other)
    {
        if(x == other.x && y==other.y)return true;
        else return false;
    }
}
