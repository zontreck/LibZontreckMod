package dev.zontreck.libzontreck.networking.packets;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public interface IPacket {
    void deserialize(CompoundTag data);

    void serialize(CompoundTag data);
    
    void toBytes(FriendlyByteBuf buf);

    boolean handle(Supplier<NetworkEvent.Context> supplier);

    /**
     * @return The network direction of the packet
     */
    NetworkDirection getDirection();

    /**
     * EXAMPLE:
     * chan.messageBuilder(S2CPlaySoundPacket.class, ModMessages.id(), getDirection())
     *       .encoder(S2CPlaySoundPacket::toBytes)
     *       .decoder(S2CPlaySoundPacket::new)
     *       .consumer(S2CPlaySoundPacket::handle)
     *       .add();
     * @param chan
     */
    void register(SimpleChannel chan);
}
