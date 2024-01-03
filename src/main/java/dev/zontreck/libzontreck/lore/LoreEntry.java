package dev.zontreck.libzontreck.lore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.ArrayList;
import java.util.List;

public class LoreEntry {

    /**
     * Builder pattern for creating a LoreEntry object.
     */
    public static class Builder {
        private boolean bold;
        private boolean italic;
        private boolean underlined;
        private boolean strikethrough;
        private boolean obfuscated;
        private String color = "";
        private String text = "";

        public Builder bold(boolean bold) {
            this.bold = bold;
            return this;
        }

        public Builder italic(boolean italic) {
            this.italic = italic;
            return this;
        }

        public Builder underlined(boolean underlined) {
            this.underlined = underlined;
            return this;
        }

        public Builder strikethrough(boolean strikethrough) {
            this.strikethrough = strikethrough;
            return this;
        }

        public Builder obfuscated(boolean obfuscated) {
            this.obfuscated = obfuscated;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public LoreEntry build() {
            return new LoreEntry(this);
        }
    }

    public boolean bold;
    public boolean italic;
    public boolean underlined;
    public boolean strikethrough;
    public boolean obfuscated;
    public String color = "";
    public String text = "";

    /**
     * Constructs a LoreEntry object from a Builder
     */
    LoreEntry(Builder builder) {
        bold = builder.bold;
        italic = builder.italic;
        underlined = builder.underlined;
        strikethrough = builder.strikethrough;
        obfuscated = builder.obfuscated;
        color = builder.color;
        text = builder.text;
    }


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

    /**
     * Saves the lore attributes into a ListTag for storage.
     *
     * @param parentTag The parent ListTag to save the attributes.
     */
    public void save(ListTag parentTag) {
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

    /**
     * Generates a JSON string representing the lore entry.
     *
     * @return The JSON string representing the lore entry.
     */
    public String saveJson() {
        List<LoreEntry> list = new ArrayList<>();
        list.add(this);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(list);
    }


    /**
     * Parses a JSON string to create a LoreEntry object.
     *
     * @param json The JSON string representing lore attributes.
     * @return A LoreEntry object created from the JSON string.
     */

    public static LoreEntry parseJson(String json) {
        Gson gson = new Gson();
        List<LoreEntry> list = gson.fromJson(json, new TypeToken<List<LoreEntry>>() {}.getType());

        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }

        return null;
    }


    /**
     * Converts a boolean value to its string representation.
     *
     * @param a The boolean value to convert.
     * @return The string representation of the boolean.
     */
    private String bool2str(boolean a) {
        return a ? "true" : "false";
    }
}
