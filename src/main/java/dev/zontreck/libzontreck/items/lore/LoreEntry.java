package dev.zontreck.libzontreck.items.lore;

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

    private String bool2str(boolean a){
        if(a)return "true";
        else return "false";
    }

    // Only json saving is available. 
    // The NBT Variant should be saved to the mod's custom tag container due to the way lore must be formatted
    public String saveJson()
    {
        
        String obj = "{";
        obj += "\"bold\": " + bool2str(bold)+",";
        obj += "\"italic\": " + bool2str(italic)+",";
        obj += "\"underlined\": "+bool2str(underlined)+",";
        obj += "\"strikethrough\": "+bool2str(strikethrough)+",";
        obj += "\"obfuscated\": "+bool2str(obfuscated)+",";
        obj += "\"color\": \""+color+"\",";
        obj += "\"text\": \""+text+"\"";
        obj += "}";


        return obj;

    }

    public LoreEntry(){}
}
