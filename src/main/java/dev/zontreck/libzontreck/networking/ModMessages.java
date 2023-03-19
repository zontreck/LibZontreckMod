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

/**
 * Networking system!
 */
public class ModMessages {
    private static SimpleChannel INSTANCE;
    private static int PACKET_ID=0;
    /**
     * INTERNAL USE ONLY. DO NOT USE THIS, IF YOU ARE NOT LIBZONTRECK!!!!!!
     * @return
     */
    public static int id()
    {
        return PACKET_ID++;
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

        net.messageBuilder(ChestGUIOpenC2S.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(ChestGUIOpenC2S::new)
            .encoder(ChestGUIOpenC2S::toBytes)
            .consumer(ChestGUIOpenC2S::handle)
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
