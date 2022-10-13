package dev.zontreck.libzontreck.chat;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.ClickEvent.Action;

/*
 * Because of some weird behavior with java not liking that both HoverEvent and ClickEvent have an Action implementation, these must both be in a custom factory here where Action can be imported by itself in both files
 */
public class Clickable {

    public static ClickEvent command(String text)
    {
        return new ClickEvent(Action.RUN_COMMAND, text);
    }
    
}
