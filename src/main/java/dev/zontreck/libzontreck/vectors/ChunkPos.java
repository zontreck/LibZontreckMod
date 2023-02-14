package dev.zontreck.libzontreck.vectors;

import net.minecraft.nbt.CompoundTag;

public class ChunkPos {
    public boolean isSubArea;
    public Points points;
    public Vector2 centerPoints;

    public ChunkPos(Vector3 point1, Vector3 point2)
    {
        isSubArea=true;
        points = new Points(point1, point2);
    }

    public ChunkPos(CompoundTag tag)
    {
        isSubArea = tag.getBoolean("subarea");
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
        tag.putBoolean("subarea", isSubArea);
        tag.put("points", points.serialize());
        tag.put("center", centerPoints.serialize());

        return tag;
    }
}
