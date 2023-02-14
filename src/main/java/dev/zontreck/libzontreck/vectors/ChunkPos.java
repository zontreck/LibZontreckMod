package dev.zontreck.libzontreck.vectors;

public class ChunkPos {
    public boolean isSubArea;
    public Points subclaim;
    
    public ChunkPos(Vector2 chunkPos)
    {
        isSubArea=false;
        Vector3 min = new Vector3(chunkPos.x, -70, chunkPos.y);
        subclaim = new Points(min, min.add(new Vector3(15, 300, 15)));
    }

    public ChunkPos(Vector3 point1, Vector3 point2)
    {
        isSubArea=true;
        subclaim = new Points(point1, point2);
    }

    public boolean isWithin(Vector3 point)
    {
        return point.inside(subclaim.Point1, subclaim.Point2);
    }
}
