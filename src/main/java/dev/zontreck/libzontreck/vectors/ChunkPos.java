package dev.zontreck.libzontreck.vectors;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

public class ChunkPos {
    public Points points;
    public Vector2 centerPoints;
    public String dim;

    public ChunkPos(Vector3 point1, Vector3 point2, ServerLevel lvl)
    {
        points = new Points(point1, point2, lvl);
        dim = WorldPosition.getDim(lvl);
    }

    public ChunkPos(CompoundTag tag)
    {
        points = new Points(tag.getCompound("points"));
        centerPoints = new Vector2(tag.getCompound("center"));
    }

    public boolean isWithin(Vector3 point)
    {
        return point.inside(points.Min, points.Max);
    }

    public static ChunkPos getChunkPos(WorldPosition pos)
    {
        return pos.getChunkPos();
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.put("points", points.serialize());
        tag.put("center", centerPoints.serialize());
        tag.putString("dim", dim);

        return tag;
    }
}
