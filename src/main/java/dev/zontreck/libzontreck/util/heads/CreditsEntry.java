package dev.zontreck.libzontreck.util.heads;

import java.util.Properties;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.items.lore.LoreContainer;
import dev.zontreck.libzontreck.items.lore.LoreEntry;
import dev.zontreck.libzontreck.util.heads.HeadCache.HeadCacheItem;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.BookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.scores.Team;

public class CreditsEntry {
    public HeadCacheItem player;
    public String name;
    public String role;
    public String description;

    public CreditsEntry(HeadCacheItem item, String name, String role, String descript){
        player=item;
        this.name=name;
        this.role=role;
        this.description=descript;
    }

    /**
     * Compiles all the information into the head item, then applies the Lore Entry with the role the person has played, and a description
     * @return
     */
    public ItemStack compile()
    {
        ItemStack stack = player.getAsItem();
        stack.setHoverName(new TextComponent(name));
        LoreContainer contain = new LoreContainer(stack);
        LoreEntry entry = new LoreEntry();
        entry.text = ChatColor.doColors("!Dark_Purple!Role: "+role);
        entry.bold=true;
        entry.italic=true;
        contain.miscData.LoreData.add(entry);
        entry = new LoreEntry();
        entry.text = ChatColor.doColors("!White!About: !Dark_Green!"+description);
        entry.italic=true;
        contain.miscData.LoreData.add(entry);

        contain.commitLore();

        return stack;
    }
    /**
     * Generates a written book that can serve as an about-page about the person being creditted.
     * @return
     */
    public ItemStack compileInfosBook()
    {
        // Will return a patchouli book
        return null;
    }
}