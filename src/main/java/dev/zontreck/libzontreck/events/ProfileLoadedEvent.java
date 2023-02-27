package dev.zontreck.libzontreck.events;

import dev.zontreck.libzontreck.profiles.Profile;
import net.minecraftforge.eventbus.api.Event;

public class ProfileLoadedEvent extends Event
{
    public Profile profile;
    public ProfileLoadedEvent(Profile prof)
    {
        profile=prof;
    }    
}
