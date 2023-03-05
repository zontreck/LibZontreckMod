package dev.zontreck.libzontreck.events;

import dev.zontreck.libzontreck.profiles.Profile;
import net.minecraftforge.eventbus.api.Event;

public class ProfileUnloadedEvent extends Event
{
    public String user_id;
    public ProfileUnloadedEvent(String id)
    {
        user_id=id;
    }    
}
