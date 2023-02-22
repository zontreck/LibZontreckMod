package dev.zontreck.libzontreck.vectors;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.exceptions.InvalidDeserialization;
import dev.zontreck.libzontreck.exceptions.InvalidSideException;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Stores a position relative to a dimension
 */
public class WorldPosition
{
    
    public Vector3 Position;
    public String Dimension;
    public String DimSafe;

    public WorldPosition(CompoundTag tag, boolean pretty) throws InvalidDeserialization
    {
        if(pretty){

            Position = new Vector3(tag.getString("Position"));
            Dimension = tag.getString("Dimension");
        }else {
            Position = new Vector3(tag.getCompound("pos"));
            Dimension = tag.getString("Dimension");
        }

        calcDimSafe();

    }

    public WorldPosition(Vector3 pos, String dim)
    {
        Position=pos;
        Dimension=dim;
        calcDimSafe();
    }

    public WorldPosition(ServerPlayer player)
    {
        this(new Vector3(player.position()), player.getLevel());
    }

    public WorldPosition(Player player)
    {
        this(new Vector3(player.position()), player.getLevel());
    }

    public WorldPosition(Vector3 pos, Level lvl)
    {
        this(pos, lvl.dimension().location().getNamespace() + ":"+lvl.dimension().location().getPath());
    }

    public WorldPosition(Vector3 pos, ServerLevel lvl)
    {
        this(pos, lvl.dimension().location().getNamespace() + ":"+lvl.dimension().location().getPath());
    }

    public void calcDimSafe()
    {
        try{

            Level lvl = getActualDimension();
            DimSafe = lvl.dimension().location().getNamespace() + "-" + lvl.dimension().location().getPath();
        }catch(InvalidSideException ex)
        {
            DimSafe="";
        }
    }
    public static String getDimSafe(Level lvl)
    {
        return lvl.dimension().location().getNamespace() + "-" + lvl.dimension().location().getPath();
    }

    /**
     * Gives you the dimension string modid:dimension
     * @param lvl
     * @return dimension string
     */
    public static String getDim(Level lvl)
    {
        return lvl.dimension().location().getNamespace() + ":" + lvl.dimension().location().getPath();
    }

    @Override
    public String toString()
    {
        return NbtUtils.structureToSnbt(serialize());
    }

    /**
     * Stores the position as a string instead of as a compound tag. Uses more memory overall
     * @return
     */
    public CompoundTag serializePretty()
    {
        CompoundTag tag = new CompoundTag();
        
        tag.putString("Position", Position.toString());
        tag.putString("Dimension", Dimension);

        return tag;
    }

    /**
     * Serializes the compound tag version of this position
     * @return
     */
    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.put("pos", Position.serialize());
        tag.putString("Dimension", Dimension);

        return tag;
    }

    

    /**
     * You must run this on the Server unless you know for a fact that the position you are requesting the dimension for is the same one the client is currently in. Otherwise you must handle the Exception properly.
     * @return The level that this worldpos stores
     * @throws InvalidSideException
     */
    public Level getActualDimension() throws InvalidSideException
    {
        
        String dim = Dimension;
        String[] dims = dim.split(":");

        ResourceLocation rl = new ResourceLocation(dims[0], dims[1]);
        Level dimL  = null;
        
        if(Minecraft.getInstance()==null)
        {

            for (Level lLevel : LibZontreck.THE_SERVER.getAllLevels()) {
                ResourceLocation XL = lLevel.dimension().location();
    
                if(XL.getNamespace().equals(rl.getNamespace())){
                    if(XL.getPath().equals(rl.getPath())){
                        dimL = lLevel;
                    }
                }
            }
        }else {
            if(getDim(Minecraft.getInstance().level) == Dimension)
            {
                return Minecraft.getInstance().level;
            }else throw new InvalidSideException("This operation must be run on the server as the client is not in the dimension you requested");
            
        }


        if(dimL == null)
        {
            LibZontreck.LOGGER.error("DIMENSION COULD NOT BE FOUND : "+Dimension);
            return null;
        }

        return dimL;
    }

    /**
     * Returns true if the other world position is identical
     * @param other
     * @return
     */
    public boolean same(WorldPosition other)
    {
        if(Position.same(other.Position) && Dimension == other.Dimension)return true;
        else return false;
    }

    /**
     * Gets the LibZontreck ChunkPosition
     * @return ChunkPos of the chunk mentioned by this worldposition, or null in the event that we are on the client and the dimension is not the same one we are inside.
     * @see WorldPosition#getActualDimension()
     */
    public ChunkPos getChunkPos()
    {
        try
        {
            Level lvl = getActualDimension();
            net.minecraft.world.level.ChunkPos mcChunk = lvl.getChunkAt(Position.asBlockPos()).getPos();
            ChunkPos pos = new ChunkPos(new Vector3(mcChunk.getMinBlockX(),-70,mcChunk.getMinBlockZ()), new Vector3(mcChunk.getMaxBlockX(), 400, mcChunk.getMaxBlockZ()), lvl);
            pos.centerPoints = new Vector2(mcChunk.getMiddleBlockX(), mcChunk.getMiddleBlockZ());
    
            return pos;
        }catch(InvalidSideException ex)
        {
            return null;
        }
    }
}
