package dev.zontreck.libzontreck.util.heads;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.lore.ExtraLore;
import dev.zontreck.libzontreck.lore.LoreContainer;
import dev.zontreck.libzontreck.lore.LoreEntry;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.util.heads.HeadCache.HeadCacheItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

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
        ItemStack stack = player.getAsItem("");
        stack.setHoverName(ChatHelpers.macro(name));
        LoreContainer contain = new LoreContainer(stack);
        contain.clear();
        LoreEntry.Builder builder = new LoreEntry.Builder();
        builder = builder.text(ChatColor.doColors("!Dark_Purple!Role: "+role + "\n"))
                .bold(true)
                .italic(true);
        contain.miscData.loreData.add(builder.build());

        builder = new LoreEntry.Builder().italic(true)
                .text(ChatColor.doColors("!White!About: !Dark_Green!"+description + "\n"));

        contain.miscData.loreData.add(builder.build());

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
