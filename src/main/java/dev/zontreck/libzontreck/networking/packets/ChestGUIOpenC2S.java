package dev.zontreck.libzontreck.networking.packets;

import java.util.function.Supplier;

import dev.zontreck.libzontreck.events.OpenGUIEvent;
import dev.zontreck.libzontreck.networking.structures.OpenGUIRequest;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

/**
 * To be used by first-party and third-party mods to assemble a menu
 * NOTE: Without the server, only the credits menu will be able to be opened, which is the only built-in menu utilizing this system.
 */
public class ChestGUIOpenC2S {
    private CompoundTag data;

    public ChestGUIOpenC2S(OpenGUIRequest request)
    {
        data = request.serialize();
    }

    public ChestGUIOpenC2S(FriendlyByteBuf buf)
    {
        data = buf.readAnySizeNbt();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeNbt(data);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context ctx = supplier.get();

        ctx.enqueueWork(()->{
            // We are on the server!
            OpenGUIRequest req = new OpenGUIRequest(data);

            MinecraftForge.EVENT_BUS.post(new OpenGUIEvent(req.ID, req.playerID));
        });

        return true;
    }
}
