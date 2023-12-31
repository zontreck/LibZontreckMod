package dev.zontreck.libzontreck.events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

import java.util.UUID;

public class GUIButtonClickedEvent extends Event
{
    public ResourceLocation id;
    public ItemStack stack;
    public UUID player;

    public GUIButtonClickedEvent(ItemStack stack, ResourceLocation id, UUID player)
    {
        this.id = id;
        this.stack = stack;
        this.player = player;
    }
}
