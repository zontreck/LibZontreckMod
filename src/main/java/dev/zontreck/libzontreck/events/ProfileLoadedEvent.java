package dev.zontreck.libzontreck.events;

import dev.zontreck.libzontreck.profiles.Profile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Event;

public class ProfileLoadedEvent extends Event
{
    public Profile profile;
    public ServerPlayer player;
    public ServerLevel level;
    public ProfileLoadedEvent(Profile prof, ServerPlayer player, ServerLevel level)
    {
        profile=prof;
        this.player=player;
        this.level=level;
    }    
}
