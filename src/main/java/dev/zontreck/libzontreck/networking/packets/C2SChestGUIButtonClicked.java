package dev.zontreck.libzontreck.networking.packets;

import dev.zontreck.libzontreck.events.GUIButtonClickedEvent;
import dev.zontreck.libzontreck.vectors.Vector2;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class C2SChestGUIButtonClicked
{
    private ItemStack stack;
    private ResourceLocation id;
    private UUID player;

    public C2SChestGUIButtonClicked(ItemStack stack, ResourceLocation id)
    {
        this.stack = stack;

        this.id = id;
        player = Minecraft.getInstance().player.getUUID();
    }

    public C2SChestGUIButtonClicked(FriendlyByteBuf buf)
    {
        stack = buf.readItem();
        id = buf.readResourceLocation();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeItem(stack);
        buf.writeResourceLocation(id);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx)
    {
        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(()->{
            // We're on the server now.

            MinecraftForge.EVENT_BUS.post(new GUIButtonClickedEvent(stack, id, player));
        });

        return true;
    }
}
