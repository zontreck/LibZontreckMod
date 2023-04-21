package dev.zontreck.libzontreck.events;

import dev.zontreck.libzontreck.vectors.WorldPosition;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class PlayerChangedPositionEvent extends Event
{
    public Player player;
    public WorldPosition position;
    public WorldPosition lastPosition;


    public PlayerChangedPositionEvent (Player current, WorldPosition pos, WorldPosition last)
    {
        player=current;
        position=pos;
        lastPosition=last;
    }
}
