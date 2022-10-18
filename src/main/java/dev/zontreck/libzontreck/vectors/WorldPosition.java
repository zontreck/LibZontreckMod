package dev.zontreck.libzontreck.vectors;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.exceptions.InvalidDeserialization;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public class WorldPosition
{
    
    public Vector3 Position;
    public String Dimension;

    public WorldPosition(CompoundTag tag, boolean pretty) throws InvalidDeserialization
    {
        if(pretty){

            Position = new Vector3(tag.getString("Position"));
            Dimension = tag.getString("Dimension");
        }else {
            Position = new Vector3(tag.getCompound("pos"));
            Dimension = tag.getString("Dimension");
        }

    }

    public WorldPosition(Vector3 pos, String dim)
    {
        Position=pos;
        Dimension=dim;
    }

    public WorldPosition(Vector3 pos, ServerLevel lvl)
    {
        Position=pos;
        Dimension = lvl.dimension().location().getNamespace() + ":"+lvl.dimension().location().getPath();
    }

    @Override
    public String toString()
    {
        return NbtUtils.structureToSnbt(serialize());
    }

    public CompoundTag serializePretty()
    {
        CompoundTag tag = new CompoundTag();
        
        tag.putString("Position", Position.toString());
        tag.putString("Dimension", Dimension);

        return tag;
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.put("pos", Position.serialize());
        tag.putString("Dimension", Dimension);

        return tag;
    }

    

    public ServerLevel getActualDimension()
    {
        
        String dim = Dimension;
        String[] dims = dim.split(":");

        ResourceLocation rl = new ResourceLocation(dims[0], dims[1]);
        ServerLevel dimL  = null;
        for (ServerLevel lServerLevel : LibZontreck.THE_SERVER.getAllLevels()) {
            ResourceLocation XL = lServerLevel.dimension().location();

            if(XL.getNamespace().equals(rl.getNamespace())){
                if(XL.getPath().equals(rl.getPath())){
                    dimL = lServerLevel;
                }
            }
        }

        if(dimL == null)
        {
            LibZontreck.LOGGER.error("DIMENSION COULD NOT BE FOUND : "+Dimension);
            return null;
        }

        return dimL;
    }

    
    public boolean same(WorldPosition other)
    {
        if(Position.same(other.Position) && Dimension == other.Dimension)return true;
        else return false;
    }
}
