package dev.zontreck.libzontreck.util;

import java.util.UUID;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.chat.ChatColor;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkHooks;

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
}
