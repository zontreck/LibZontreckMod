package dev.zontreck.libzontreck.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.chat.ChatColor;
import net.minecraft.network.chat.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ChatHelpers {
    public static void broadcastActionBar(Component message, MinecraftServer server)
    {
        server.execute(new Runnable(){
            @Override
            public void run()
            {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    player.displayClientMessage(message, true);
                }
                LibZontreck.LOGGER.info("[ALL] "+message.getContents());
            }
        });
    }

    public static void broadcast(Component message, MinecraftServer server)
    {
        server.execute(new Runnable(){
            @Override
            public void run()
            {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    player.displayClientMessage(message, false);
                }
                LibZontreck.LOGGER.info("[ALL] "+message.getContents());
            }
        });
    }

    public static void broadcastTo(UUID ID, Component message, MinecraftServer server, boolean actionBar)
    {
        server.execute(new Runnable(){
            @Override
            public void run()
            {
                ServerPlayer play = server.getPlayerList().getPlayer(ID);
                if(play==null)return;


                play.displayClientMessage(message, actionBar);

                LibZontreck.LOGGER.info("[SERVER] -> ["+play.getName().getContents()+"] "+message.getContents());
            }
        });
    }

    public static void broadcastTo(UUID ID, Component message, MinecraftServer server)
    {
        broadcastTo(ID, message, server, false);
    }

    public static void broadcastToAbove(UUID ID, Component message, MinecraftServer server)
    {
        broadcastTo(ID, message, server, true);
    }
    public static void broadcastTo(Player ID, Component message, MinecraftServer server)
    {
        broadcastTo(ID.getUUID(), message, server, false);
    }

    public static void broadcastToAbove(Player ID, Component message, MinecraftServer server)
    {
        broadcastTo(ID.getUUID(), message, server, true);
    }

    public static String hashOfMd5(String input) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("md5");
        md5.update(input.getBytes());
        return asHex(md5.digest());
    }
    public static String hashOfSha256(String input) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("sha-256");
        md5.update(input.getBytes());
        return asHex(md5.digest());
    }

    public static String hashOfMd5(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("md5");
        md5.update(input);
        return asHex(md5.digest());
    }
    public static String hashOfSha256(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("sha-256");
        md5.update(input);
        return asHex(md5.digest());
    }

    public static String asHex(byte[] input)
    {
        return BinUtil.bytesToHex(input);
    }

    /**
     * Returns the output with colors applied, and chat entries replaced using [number] as the format
     * @param input
     * @param inputs Entries to replace with in input
     * @return
     */
    public static MutableComponent macro(String input, String... inputs)
    {
        return new TextComponent(macroize(input,inputs));
    }
    /**
     * Returns the output with colors applied, and chat entries replaced using [number] as the format
     * @param input
     * @param inputs Entries to replace with in input
     * @return
     */
    public static String macroize(String input, String... inputs)
    {
        String output = input;
        for (int i = 0; i < inputs.length; i++) {
            String inp = inputs[i];

            output = output.replaceAll("\\["+String.valueOf(i)+"]", inp);
        }

        return ChatColor.doColors(output);
    }

    /**
     * Merges the styles
     * @param comp The original component
     * @param ce Click event to add to the component
     * @return Component + Merged Event
     */
    public static MutableComponent applyClickEvent(MutableComponent comp, ClickEvent ce)
    {
        return comp.setStyle(comp.getStyle().withClickEvent(ce));
    }

    /**
     * Merges the styles
     * @param comp The original component
     * @param ce Hover event to add to the component
     * @return Component + Merged Event
     */
    public static MutableComponent applyHoverEvent(MutableComponent comp, HoverEvent ce)
    {
        return comp.setStyle(comp.getStyle().withHoverEvent(ce));
    }
}
