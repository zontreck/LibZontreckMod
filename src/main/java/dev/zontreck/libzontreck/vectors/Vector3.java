package dev.zontreck.libzontreck.vectors;

import java.util.ArrayList;
import java.util.List;

import dev.zontreck.libzontreck.exceptions.InvalidDeserialization;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

public class Vector3 
{
    public double x;
    public double y;
    public double z;
    
    public Vec3 asMinecraftVector(){
        return new Vec3(x, y, z);
    }

    public BlockPos asBlockPos()
    {
        return new BlockPos(asMinecraftVector());
    }

    public Vector3()
    {

    }

    public Vector3(double x, double y, double z)
    {
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public Vector3(Vec3 pos)
    {
        x=pos.x;
        y=pos.y;
        z=pos.z;
    }

    public Vector3(BlockPos pos)
    {
        x=pos.getX();
        y=pos.getY();
        z=pos.getZ();
    }

    public Vector3(String pos) throws InvalidDeserialization
    {
        // This will be serialized most likely from the ToString method
        // Parse
        if(pos.startsWith("<"))
        {
            pos=pos.substring(1, pos.length()-1); // Rip off the ending bracket too
            String[] positions = pos.split(", ");
            if(positions.length!=3)
            {
                positions = pos.split(",");
            }

            if(positions.length!=3)
            {
                throw new InvalidDeserialization("Positions must be in the same format provided by ToString() (ex. <1,1,1> or <1, 1, 1>");
            }

            this.x = Double.parseDouble(positions[0]);
            this.y = Double.parseDouble(positions[1]);
            this.z = Double.parseDouble(positions[2]);
            // We are done now
        }
    }

    public List<Vector3> makeCube(Vector3 other)
    {
        List<Vector3> vecs = new ArrayList<>();
        Vector3 work = new Vector3();

        double xx = x;
        double yy = y;
        double zz = z;

        int yState = 0;
        int zState = 0;
        int xState = 0;

        for(xx = Math.round(x); (xx != Math.round(other.x) && xState != 2);)
        {
            for(zz = Math.round(z); (zz != Math.round(other.z) && zState != 2);)
            {
                for(yy = Math.round(y); (yy != Math.round(other.y) && yState != 2);)
                {
                    work = new Vector3(xx, yy, zz);

                    if(!vecs.contains(work)) vecs.add(work);

                    if(yy > other.y)
                    {
                        yy -= 1.0;
                        if(yy == Math.round(other.y) && yState == 0)
                        {
                            yState++;
                        }else{
                            if(yState == 1)
                            {
                                yState ++;
                            }
                        }
                    } else if(yy < other.y)
                    {
                        yy += 1.0;
                        if(yy == Math.round(other.y) && yState == 0){
                            yState ++;
                        }else {
                            if(yState == 1)yState++;
                        }
                    }
                }

                yState=0;
                work = new Vector3(xx,yy,zz);
                
                if(!vecs.contains(work)) vecs.add(work);

                if(zz > other.z)
                {
                    zz -= 1.0;

                    if(zz == Math.round(other.z) && zState == 0)zState++;
                    else{
                        if(zState == 1)zState++;
                    }
                }else if(zz < other.z)
                {
                    zz += 1.0;

                    if(zz == Math.round(other.z) && zState == 0)zState++;
                    else {
                        if(zState==1)zState++;
                    }
                }
            }

            zState=0;
            work = new Vector3(xx,yy,zz);

            if(!vecs.contains(work)) vecs.add(work);

            if(xx > other.x)
            {
                xx -= 1.0;

                if(xx == Math.round(other.x) && xState == 0) xState++;
                else{
                    if(xState == 1)xState++;
                }
            }else if(xx < other.x)
            {
                xx += 1.0;

                if(xx == Math.round(other.x)  &&  xState==0)xState++;
                else{
                    if(xState==1)xState++;
                }
            }
        }

        return vecs;
    }

    public Vector3 subtract(Vector3 other)
    {
        return new Vector3(x-other.x, y-other.y, z-other.z);
    }
    public Vector3 add(Vector3 other)
    {
        return new Vector3(x+other.x, y+other.y, z +other.z);
    }

    public double distance(Vector3 other)
    {
        Vector3 sub = subtract(other);
        return Math.sqrt((sub.x * sub.x + sub.y * sub.y + sub.z * sub.z));
    }

    public Vector3 moveUp()
    {
        Vector3 up = Clone();
        up.y+=1;
        return up;
    }
    public Vector3 moveDown()
    {
        Vector3 up = Clone();
        up.y-=1;
        return up;
    }


    public Vector3 Clone()
    {
        Vector3 n = new Vector3(x, y, z);
        return n;
    }

    @Override
    public String toString()
    {
        return "<"+String.valueOf(x)+", "+String.valueOf(y)+", "+String.valueOf(z)+">";
    }

    public NonAbsVector3 rounded()
    {
        NonAbsVector3 cl = new NonAbsVector3(this);
        return cl;
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("x", x);
        tag.putDouble("y", y);
        tag.putDouble("z", z);

        return tag;
    }

    public Vector3(CompoundTag tag) {
        this.deserialize(tag);
    }
    public void deserialize(CompoundTag tag)
    {
        x=tag.getDouble("x");
        y=tag.getDouble("y");
        z=tag.getDouble("z");
    }

    
    public boolean same(Vector3 other)
    {
        if(x == other.x && y==other.y && z==other.z)return true;
        else return false;
    }
}
