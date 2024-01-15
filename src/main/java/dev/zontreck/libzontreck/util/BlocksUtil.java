package dev.zontreck.libzontreck.util;

import dev.zontreck.libzontreck.vectors.Vector3;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains helper functions for block position calculations
 */
public class BlocksUtil
{
    /**
     * Gathers a list of positions for like-blocks in a vein. This can accept a limit of -1, but has a hard cap at 512 blocks
     * @param level The level to find the vein in
     * @param start Starting position for vein
     * @param limit The applicable limit for vein detection
     * @return List of positions for the vein
     */
    public static List<Vector3> VeinOf(ServerLevel level, Vector3 start, int limit)
    {
        List<Vector3> ret = new ArrayList<>();



        return ret;
    }
}
