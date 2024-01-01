package dev.zontreck.libzontreck.vectors;

import dev.zontreck.libzontreck.exceptions.InvalidDeserialization;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec2;

public class Vector2i
{
    public static final Vector2i ZERO = new Vector2i(0, 0);

    public int x;
    public int y;

    public Vec2 asMinecraftVector(){
        return new Vec2(x, y);
    }

    public Vector2i()
    {

    }

    public Vector2i(int x, int y)
    {
        this.x=x;
        this.y=y;
    }

    public Vector2i(Vec2 pos)
    {
        x= (int) Math.floor(pos.x);
        y=(int)Math.floor(pos.y);
    }

    public Vector2i(String pos) throws InvalidDeserialization
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

            this.x = Integer.parseInt(positions[0]);
            this.y = Integer.parseInt(positions[1]);
            // We are done now
        }
    }

    public Vector2i Clone()
    {
        Vector2i n = new Vector2i(x, y);
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

    public Vector2i(CompoundTag tag) {
        this.deserialize(tag);
    }
    public void deserialize(CompoundTag tag)
    {
        x=tag.getInt("x");
        y=tag.getInt("y");
    }

    public boolean same(Vector2i other)
    {
        if(x == other.x && y==other.y)return true;
        else return false;
    }

    public boolean inside(Vector2i point1, Vector2i point2)
    {
        if(point1.x <= x && point2.x >= x){
            if(point1.y <= y && point2.y >= y)
            {
                return true;
            }
        }

        return false;
    }

    public boolean greater(Vector2i other)
    {
        return ((x>other.x) && (y>other.y));
    }
    public boolean less(Vector2i other)
    {
        return ((x>other.x) && (y>other.y));
    }
    public boolean equal(Vector2i other)
    {
        return same(other);
    }
}
