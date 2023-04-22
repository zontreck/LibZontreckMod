package dev.zontreck.libzontreck.util;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.exceptions.InvalidSideException;
import dev.zontreck.libzontreck.networking.ModMessages;
import dev.zontreck.libzontreck.networking.packets.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public class ServerUtilities
{
    /**
     * This function only exists on the server
     * @param id Player ID
     * @return The server player associated with the ID
     */
    public static ServerPlayer getPlayerByID(String id)
    {
        return LibZontreck.THE_SERVER.getPlayerList().getPlayer(UUID.fromString(id));
    }
    
    /**
     * Handles the registration of packets
     * @param <X>
     * @param channel
     * @param type
     * @param inst
     * @param decoder
     */
    public static <X extends IPacket> void registerPacket(SimpleChannel channel, Class<X> type, X inst, Function<FriendlyByteBuf, X> decoder)
    {
        IPacket packet = (IPacket) inst;
        channel.messageBuilder(type, ModMessages.id(), packet.getDirection())
            .decoder(decoder)
            .encoder(X::toBytes)
            .consumerMainThread(X::handle)
            .add();
    }

    /**
     * Handles the tedious and repetitive actions in the handle packet segment of code
     * @param context
     * @param run
     * @return
     */
    public static boolean handlePacket(Supplier<NetworkEvent.Context> context, Runnable run)
    {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(run);

        return true;
    }

    /**
     * Checks if the mod is running on the server
     * @return True if the mod is on the server
     */
    public static boolean isServer()
    {
        return (LibZontreck.CURRENT_SIDE == LogicalSide.SERVER);
    }

    /**
     * Checks if the mod is running on the client
     * @return True if the mod is on the client
     */
    public static boolean isClient()
    {
        return !isServer();
    }

    public static boolean playerIsOffline(UUID ID) throws InvalidSideException {
        if(isClient())throw new InvalidSideException("This can only be called on the server");

        if(LibZontreck.THE_SERVER.getPlayerList().getPlayer(ID) == null) return true;
        else return false;
    }
}