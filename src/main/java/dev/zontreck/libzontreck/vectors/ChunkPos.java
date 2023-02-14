package dev.zontreck.libzontreck.vectors;

public class ChunkPos {
    public boolean isSubArea;
    public Points subclaim;
    

    public ChunkPos(Vector3 point1, Vector3 point2)
    {
        isSubArea=true;
        subclaim = new Points(point1, point2);
    }

    public boolean isWithin(Vector3 point)
    {
        return point.inside(subclaim.Point1, subclaim.Point2);
    }

    public static ChunkPos getChunkPos(WorldPosition pos)
    {
        return pos.getChunkPos();
    }
}
