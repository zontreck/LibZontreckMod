package dev.zontreck.libzontreck.networking.packets;

import java.util.function.Supplier;

import dev.zontreck.libzontreck.util.BinUtil;
import dev.zontreck.libzontreck.util.ServerUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.simple.SimpleChannel;

public class S2CPlaySoundPacket implements IPacket
{
    public ResourceLocation sound;

    public S2CPlaySoundPacket(FriendlyByteBuf buf) {
        sound = buf.readResourceLocation();
    }

    public S2CPlaySoundPacket(ResourceLocation loc) {
        sound=loc;
    }

    @Override
    public void deserialize(CompoundTag data) {
        // Deserializes the play sound packet
        throw new UnsupportedOperationException("This operation is not supported in the play sound packet!");
    }

    @Override
    public void serialize(CompoundTag data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'serialize'");
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(sound);
    }

    @Override
    public boolean handle(Supplier<Context> supplier) {
        NetworkEvent.Context ctx=supplier.get();

        ctx.enqueueWork(()->{
            // We are on the client now, enqueue the sound!
            SoundEvent ev = SoundEvent.createFixedRangeEvent(sound, 2.0f);
            // Play sound for player!
            Minecraft.getInstance().player.playSound(ev, 1, BinUtil.getARandomInstance().nextFloat(0, 1));
        });

        return true;
    }

    @Override
    public NetworkDirection getDirection() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }

    @Override
    public void register(SimpleChannel chan) {
        ServerUtilities.registerPacket(chan, S2CPlaySoundPacket.class, this, S2CPlaySoundPacket::new);
    }
    
}
