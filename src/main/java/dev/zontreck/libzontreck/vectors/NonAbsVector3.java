package dev.zontreck.libzontreck.vectors;

/*
* This is a non-serializable instanced Vector that is meant to slam positions down as a integer
*/
public class NonAbsVector3
{
    public long x;
    public long y;
    public long z;

    public NonAbsVector3(Vector3 origin)
    {
        x = Math.round(origin.x);
        y = Math.round(origin.y);
        z = Math.round(origin.z);
    }
}
