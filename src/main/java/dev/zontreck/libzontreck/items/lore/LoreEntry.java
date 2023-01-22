package dev.zontreck.libzontreck.items.lore;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class LoreEntry
{
    public boolean bold;
    public boolean italic;
    public boolean underlined;
    public boolean strikethrough;
    public boolean obfuscated;
    public String color="";
    public String text="";

    public LoreEntry (CompoundTag tag)
    {

        bold = tag.getBoolean("bold");
        italic = tag.getBoolean("italic");
        underlined = tag.getBoolean("underlined");
        strikethrough = tag.getBoolean("strikethrough");
        obfuscated = tag.getBoolean("obfuscated");
        color = tag.getString("color");
        text = tag.getString("text");
    }

    public void save(ListTag parentTag){
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("bold", bold);
        tag.putBoolean("italic", italic);
        tag.putBoolean("underlined", underlined);
        tag.putBoolean("strikethrough", strikethrough);
        tag.putBoolean("obfuscated", obfuscated);
        tag.putString("color", color);
        tag.putString("text", text);

        parentTag.add(tag);
    }

    // Only json saving is available. 
    // The NBT Variant should be saved to the mod's custom tag container due to the way lore must be formatted
    public JsonObjectBuilder saveJson()
    {
        JsonObjectBuilder obj = Json.createObjectBuilder();
        obj.add("bold", bold);
        obj.add("italic", italic);
        obj.add("underlined", underlined);
        obj.add("strikethrough", strikethrough);
        obj.add("obfuscated", obfuscated);
        obj.add("color", color);
        obj.add("text", text);


        return obj;

    }

    public LoreEntry(){}
}
