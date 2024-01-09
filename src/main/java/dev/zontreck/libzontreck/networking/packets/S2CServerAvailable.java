package dev.zontreck.libzontreck.networking.packets;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.networking.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CServerAvailable
{
    public S2CServerAvailable(FriendlyByteBuf buf)
    {
        // nothing!
    }

    public S2CServerAvailable()
    {

    }

    public void toBytes(FriendlyByteBuf buf)
    {

    }

    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(()->{
            // We are now on the client

            LibZontreck.LIBZONTRECK_SERVER_AVAILABLE = true;
        });
    }

    public void send(ServerPlayer player)
    {
        ModMessages.sendToPlayer(this, player);
    }
}
