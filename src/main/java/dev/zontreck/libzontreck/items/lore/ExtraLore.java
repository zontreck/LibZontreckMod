package dev.zontreck.libzontreck.items.lore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

// Extra container
public class ExtraLore {
    public List<LoreEntry> LoreData = new ArrayList<>();

    
    public void save(CompoundTag tag)
    {
        ListTag lores = new ListTag();
        for (LoreEntry loreEntry : LoreData) {
            loreEntry.save(lores);
        }
        // Extra entry in display:Lore list
        tag.put("extra", lores);
    }

    public ListTag save(){
        ListTag lores = new ListTag();
        for (LoreEntry tag : LoreData) {
            tag.save(lores);
        }
        return lores;
    }

    // This json object is what goes inside the actual item lore. This is not the entry used to save the state
    public String saveJson(){
        String ret = "";
        JsonObjectBuilder loreEntry = Json.createObjectBuilder();
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (LoreEntry loreEntryx : LoreData) {
            jab.add(loreEntryx.saveJson());
        }
        loreEntry.add("extra", jab);
        loreEntry.add("text", "");
        ret=loreEntry.build().toString();
        return ret;
    }

    public ExtraLore(CompoundTag tags)
    {
        ListTag tag = tags.getList("extra", CompoundTag.TAG_COMPOUND);
        Iterator<Tag> tagsx = tag.iterator();
        while(tagsx.hasNext())
        {
            Tag t = tagsx.next();
            CompoundTag ct = (CompoundTag)t;
            LoreData.add(new LoreEntry(ct));
        }
    }

    public ExtraLore()
    {

    }
}

