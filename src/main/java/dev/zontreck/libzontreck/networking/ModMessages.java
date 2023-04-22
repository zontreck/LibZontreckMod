package dev.zontreck.libzontreck.networking;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.events.RegisterPacketsEvent;
import dev.zontreck.libzontreck.networking.packets.ChestGUIOpenC2S;
import dev.zontreck.libzontreck.networking.packets.IPacket;
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

        net.messageBuilder(ChestGUIOpenC2S.class, PACKET_ID.getAndIncrement(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(ChestGUIOpenC2S::new)
            .encoder(ChestGUIOpenC2S::toBytes)
            .consumerMainThread(ChestGUIOpenC2S::handle)
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
