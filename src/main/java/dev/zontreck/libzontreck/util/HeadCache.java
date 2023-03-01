package dev.zontreck.libzontreck.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dev.zontreck.libzontreck.LibZontreck;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PlayerHeadItem;

public class HeadCache
{
    public static final Path CACHE_FILE;
    public static final HeadCache CACHE = new HeadCache();
    public List<HeadCacheItem> items = new ArrayList<>();

    public class HeadCacheItem
    {
        public UUID owner;
        public String texture;
        public String name;

        public CompoundTag serialize()
        {
            CompoundTag tag = new CompoundTag();
            tag.putUUID("id", owner);
            tag.putString("texture", texture);
            tag.putString("name", name);

            return tag;
        }

        public HeadCacheItem(CompoundTag tag)
        {
            owner = tag.getUUID("id");
            texture = tag.getString("texture");
            name = tag.getString("name");
        }

        private HeadCacheItem()
        {}

        public ItemStack getAsItem()
        {
            ItemStack head = new ItemStack(Items.PLAYER_HEAD, 1);

            CompoundTag skullOwner = new CompoundTag();
            skullOwner.putUUID("Id", owner);

            CompoundTag properties = new CompoundTag();
            ListTag textures = new ListTag();
            CompoundTag item = new CompoundTag();
            item.putString("Value", texture);
            textures.add(item);
            properties.put("textures", textures);

            skullOwner.put("Properties", properties);
            head.addTagElement(PlayerHeadItem.TAG_SKULL_OWNER, skullOwner);

            TextComponent headname = ChatHelpers.macro("!Dark_red![0]'s Head", name);
            head.setHoverName(headname);

            return head;
            
        }

        public static UUID toNewID(final String input) {
            return UUID.fromString(input.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
        }
        public String getOldID()
        {
            return owner.toString().replaceAll("-", "");
        }
    }

    static{
        CACHE_FILE = LibZontreck.BASE_CONFIG.resolve("head_cache.nbt");

        if(CACHE_FILE.toFile().exists())
        {
            // Deserialize heads
            try {
                CompoundTag tag = NbtIo.read(CACHE_FILE.toFile());
                CACHE.initFromCache(tag);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            CACHE.resetCache();
        }
    }

    private void initFromCache(CompoundTag tag)
    {
        ListTag heads = tag.getList("heads", Tag.TAG_COMPOUND);

        for (Tag tag2 : heads) {
            CompoundTag tag3 = (CompoundTag)tag2;
            items.add(new HeadCacheItem(tag3));
        }
    }

    public void saveCache()
    {
        try {
            NbtIo.write(serialize(), CACHE_FILE.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CompoundTag serialize()
    {
        ListTag heads = new ListTag();
        for (HeadCacheItem item : items) {
            heads.add(item.serialize());
        }

        CompoundTag tag = new CompoundTag();
        tag.put("heads", heads);

        return tag;
    }

    /**
     * Adds a new head to the cache
     * @param owner
     * @param texture
     * @param name
     * @return The item added to the cache
     * @return Null if not added!
     */
    public HeadCacheItem addToCache(UUID owner, String texture, String name)
    {
        HeadCacheItem item = new HeadCacheItem();
        item.name=name;
        item.texture=texture;
        item.owner=owner;
        if(!hasHead(name))
        {
            items.add(item);
            saveCache();
            return item;
        }
        return null;
    }

    /**
     * Initializes the cache fresh using the default heads for the developer(s), contributors/testers, and patreon supporters
     */
    public void resetCache()
    {
        HeadUtilities.get("zontreck");
        HeadUtilities.get("PossumTheWarrior");
        HeadUtilities.get("GemMD");
    }

    public boolean hasHead(String playerName)
    {
        for (HeadCacheItem item : items) {
            if(item.name.equals(playerName)) return true;
        }
        return false;
    }


    public HeadCacheItem getHead(String playerName)
    {
        for (HeadCacheItem item : items) {
            if(item.name.equals(playerName)) return item;
        }
        return null;
    }
}
