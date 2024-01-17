package dev.zontreck.libzontreck.vectors;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.exceptions.InvalidDeserialization;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class WorldPosition {

    public Vector3 Position;
    public String Dimension;
    public String DimSafe;

    public WorldPosition(CompoundTag tag, boolean pretty) throws InvalidDeserialization {
        if (pretty) {

            Position = new Vector3(tag.getString("Position"));
            Dimension = tag.getString("Dimension");
        } else {
            Position = new Vector3(tag.getCompound("pos"));
            Dimension = tag.getString("Dimension");
        }

        calcDimSafe();

    }

    public WorldPosition(Vector3 pos, String dim) {
        Position = pos;
        Dimension = dim;
        calcDimSafe();
    }

    public WorldPosition(ServerPlayer player) {
        this(new Vector3(player.position()), player.getLevel());
    }

    public WorldPosition(Vector3 pos, ServerLevel lvl) {
        Position = pos;
        Dimension = lvl.dimension().location().getNamespace() + ":" + lvl.dimension().location().getPath();
        calcDimSafe();
    }

    public void calcDimSafe() {
        ServerLevel lvl = getActualDimension();
        DimSafe = lvl.dimension().location().getNamespace() + "-" + lvl.dimension().location().getPath();
    }

    public static String getDimSafe(ServerLevel lvl) {
        return lvl.dimension().location().getNamespace() + "-" + lvl.dimension().location().getPath();
    }

    /**
     * Gives you the dimension string modid:dimension
     *
     * @param lvl
     * @return dimension string
     */
    public static String getDim(ServerLevel lvl) {
        return lvl.dimension().location().getNamespace() + ":" + lvl.dimension().location().getPath();
    }


    @Override
    public String toString() {
        return NbtUtils.structureToSnbt(serialize());
    }

    public CompoundTag serializePretty() {
        CompoundTag tag = new CompoundTag();

        tag.putString("Position", Position.toString());
        tag.putString("Dimension", Dimension);

        return tag;
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.put("pos", Position.serialize());
        tag.putString("Dimension", Dimension);

        return tag;
    }


    public ServerLevel getActualDimension() {

        String dim = Dimension;
        String[] dims = dim.split(":");

        ResourceLocation rl = new ResourceLocation(dims[0], dims[1]);
        ServerLevel dimL = null;
        for (ServerLevel lServerLevel : LibZontreck.THE_SERVER.getAllLevels()) {
            ResourceLocation XL = lServerLevel.dimension().location();

            if (XL.getNamespace().equals(rl.getNamespace())) {
                if (XL.getPath().equals(rl.getPath())) {
                    dimL = lServerLevel;
                }
            }
        }

        if (dimL == null) {
            LibZontreck.LOGGER.error("DIMENSION COULD NOT BE FOUND : " + Dimension);
            return null;
        }

        return dimL;
    }


    public boolean same(WorldPosition other) {
        return Position.same(other.Position) && Dimension == other.Dimension;
    }

    public ChunkPos getChunkPos() {
        net.minecraft.world.level.ChunkPos mcChunk = getActualDimension().getChunkAt(Position.asBlockPos()).getPos();
        ChunkPos pos = new ChunkPos(new Vector3(mcChunk.getMinBlockX(), -70, mcChunk.getMinBlockZ()), new Vector3(mcChunk.getMaxBlockX(), 400, mcChunk.getMaxBlockZ()), getActualDimension());
        pos.centerPoints = new Vector2(mcChunk.getMiddleBlockX(), mcChunk.getMiddleBlockZ());

        return pos;
    }
}
