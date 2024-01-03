package dev.zontreck.libzontreck.lore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import dev.zontreck.libzontreck.lore.LoreEntry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

/**
 * Represents a container for multiple LoreEntry instances.
 */
public class ExtraLore {
    @SerializedName("extra")
    public List<LoreEntry> loreData = new ArrayList<>();

    /**
     * Saves the lore entries into a CompoundTag for storage.
     *
     * @param tag The CompoundTag to save the entries.
     */
    public void save(CompoundTag tag) {
        ListTag lores = saveEntries();
        // Extra entry in display:Lore list
        tag.put("extra", lores);
    }

    /**
     * Saves the lore entries into a ListTag.
     *
     * @return The ListTag containing saved lore entries.
     */
    public ListTag saveEntries() {
        ListTag lores = new ListTag();
        for (LoreEntry loreEntry : loreData) {
            lores.add(StringTag.valueOf(loreEntry.saveJson()));
        }
        return lores;
    }

    /**
     * Generates a JSON string representing the lore entries.
     *
     * @return The JSON string representing the lore entries.
     */
    public String saveJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }


    /**
     * Parses a JSON string to create an ExtraLore object with LoreEntry instances.
     *
     * @param json The JSON string representing lore entries.
     * @return An ExtraLore object created from the JSON string.
     */
    public static ExtraLore parseJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ExtraLore.class);
    }

    /**
     * Constructs an ExtraLore object from a CompoundTag.
     *
     * @param tags The CompoundTag containing lore entries.
     */
    public ExtraLore(CompoundTag tags) {
        ListTag tag = tags.getList("extra", CompoundTag.TAG_COMPOUND);
        for (Tag t : tag) {
            CompoundTag ct = (CompoundTag) t;
            loreData.add(new LoreEntry(ct));
        }
    }

    public ExtraLore(ListTag tag)
    {
        for(Tag t : tag)
        {
            loreData.add(LoreEntry.parseJson(t.getAsString()));
        }
    }

    /**
     * Constructs an empty ExtraLore object.
     */
    public ExtraLore() {
        // Empty constructor
    }
}
