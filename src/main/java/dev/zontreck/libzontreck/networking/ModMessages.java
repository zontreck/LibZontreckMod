package dev.zontreck.libzontreck.networking;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.events.RegisterPacketsEvent;
import dev.zontreck.libzontreck.networking.packets.IPacket;
import dev.zontreck.libzontreck.networking.packets.S2CCloseChestGUI;
import dev.zontreck.libzontreck.networking.packets.S2CPlaySoundPacket;
import dev.zontreck.libzontreck.networking.packets.S2CServerAvailable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Networking system!
 */
public class ModMessages {
    private static SimpleChannel INSTANCE;
    /**
     * INTERNAL USE ONLY
     */
    private static AtomicInteger PACKET_ID=new AtomicInteger(0);
    public static int id()
    {
        return PACKET_ID.getAndIncrement();
    }
    public static void register()
    {
        SimpleChannel net = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(LibZontreck.MOD_ID, "messages"))
            .networkProtocolVersion(()->"1.0")
            .clientAcceptedVersions(s->true)
            .serverAcceptedVersions(s->true)
            .simpleChannel();
        
        RegisterPacketsEvent event = new RegisterPacketsEvent();
        MinecraftForge.EVENT_BUS.post(event);

        INSTANCE=net;

        for(IPacket packet : event.packets)
        {
            packet.register(net);
        }

        net.messageBuilder(S2CPlaySoundPacket.class, PACKET_ID.getAndIncrement(), NetworkDirection.PLAY_TO_CLIENT)
                        .decoder(S2CPlaySoundPacket::new)
                        .encoder(S2CPlaySoundPacket::toBytes)
                        .consumerMainThread(S2CPlaySoundPacket::handle)
                        .add();

        net.messageBuilder(S2CCloseChestGUI.class, PACKET_ID.getAndIncrement(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(S2CCloseChestGUI::new)
                .encoder(S2CCloseChestGUI::toBytes)
                .consumerMainThread(S2CCloseChestGUI::handle)
                .add();

        net.messageBuilder(S2CServerAvailable.class, PACKET_ID.getAndIncrement(),
                NetworkDirection.PLAY_TO_CLIENT)
                .decoder(S2CServerAvailable::new)
                .encoder(S2CServerAvailable::toBytes)
                .consumerMainThread(S2CServerAvailable::handle)
                .add();

    }


    public static <MSG> void sendToServer(MSG message){
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player)
    {
        INSTANCE.send(PacketDistributor.PLAYER.with(()->player), message);
    }

    public static <MSG> void sendToAll(MSG message)
    {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
