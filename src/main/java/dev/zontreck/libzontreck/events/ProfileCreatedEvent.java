package dev.zontreck.libzontreck.events;

import dev.zontreck.libzontreck.profiles.Profile;
import net.minecraftforge.eventbus.api.Event;

public class ProfileCreatedEvent extends Event
{
    public String playerID;
    public ProfileCreatedEvent(Profile newProfile)
    {
        playerID = newProfile.user_id;
    }    
}
