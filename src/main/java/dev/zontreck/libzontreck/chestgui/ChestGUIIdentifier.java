package dev.zontreck.libzontreck.chestgui;

import net.minecraft.nbt.CompoundTag;

import java.util.Objects;
import java.util.UUID;

/**
 * As of revision 0121, this is now used in place of ResourceLocation. This gives more flexibility without the restrictions imposed on naming of ResourceLocations
 */
public class ChestGUIIdentifier
{
    public UUID ID;
    public String nickname;

    /**
     * Default constructor. Generates a random UUID!
     * @param nick GUI nickname
     */
    public ChestGUIIdentifier(String nick)
    {
        ID = UUID.randomUUID();
        nickname=nick;
    }

    /**
     * Serialize out the identifier for transmitting over the network, or saving to disk
     * @return
     */
    public CompoundTag serialize()
    {
        CompoundTag ret = new CompoundTag();
        ret.putUUID("id", ID);
        ret.putString("name", nickname);

        return ret;
    }

    /**
     * Deserialize the identifier
     * @param tag
     */
    public ChestGUIIdentifier(CompoundTag tag)
    {
        ID = tag.getUUID("id");
        nickname = tag.getString("name");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChestGUIIdentifier that = (ChestGUIIdentifier) o;
        return Objects.equals(ID, that.ID) && Objects.equals(nickname, that.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, nickname);
    }
}
