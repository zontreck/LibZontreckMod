package dev.zontreck.libzontreck.chat;

import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.HoverEvent.Action;
import net.minecraft.world.item.ItemStack;

/*
 * Because of some weird behavior with java not liking that both HoverEvent and ClickEvent have an Action implementation, these must both be in a custom factory here where Action can be imported by itself in both files
 */
public class HoverTip {

    /**
     * This will return a hover event that shows a string of text when hovered over
     * @param text
     * @return
     */
    public static HoverEvent get(String text)
    {
        return new HoverEvent(Action.SHOW_TEXT, new TextComponent(text));
    }

    /**
     * Returns a hover event that shows an item when hovered over
     * @param is
     * @return
     */
    public static HoverEvent getItem(ItemStack is)
    {
        return new HoverEvent(Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(is));
    }
    
}
