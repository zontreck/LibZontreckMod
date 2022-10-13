package dev.zontreck.libzontreck.chat;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.HoverEvent.Action;

/*
 * Because of some weird behavior with java not liking that both HoverEvent and ClickEvent have an Action implementation, these must both be in a custom factory here where Action can be imported by itself in both files
 */
public class HoverTip {

    public static HoverEvent get(String text)
    {
        return new HoverEvent(Action.SHOW_TEXT, Component.literal(text));
    }
    
}
