package dev.zontreck.libzontreck.events;

import dev.zontreck.libzontreck.profiles.Profile;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class ProfileUnloadingEvent extends Event
{
    public Profile profile;
    public ServerPlayer player;
    
    public ProfileUnloadingEvent(Profile profile, ServerPlayer player)
    {
        this.profile=profile;
        this.player=player;
    }
}
