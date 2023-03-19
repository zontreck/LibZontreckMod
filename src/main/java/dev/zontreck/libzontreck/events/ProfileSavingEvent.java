package dev.zontreck.libzontreck.events;

import dev.zontreck.libzontreck.profiles.Profile;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.eventbus.api.Event;

/**
 * This class is not cancelable.
 * This event is fired while the profile is saving. It is used to acquire misc data.
 * 
 * The only part of this that is modifiable at this stage of saving is the tag.
 */
public class ProfileSavingEvent extends Event
{
    public final Profile profile;
    public CompoundTag tag;

    public ProfileSavingEvent(Profile profile, CompoundTag tag)
    {
        this.profile=profile;
        this.tag=tag;
    }
}
