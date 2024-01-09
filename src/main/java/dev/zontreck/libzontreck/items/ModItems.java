package dev.zontreck.libzontreck.items;

import dev.zontreck.libzontreck.LibZontreck;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems
{
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, LibZontreck.MOD_ID);

    public static final RegistryObject<Item> CHESTGUI_ADD = REGISTRY.register("chestgui_add", ()->new Item(new Item.Properties()));

    public static final RegistryObject<Item> CHESTGUI_REM = REGISTRY.register("chestgui_remove", ()->new Item(new Item.Properties()));

    public static final RegistryObject<Item> CHESTGUI_BACK = REGISTRY.register("chestgui_back", ()->new Item(new Item.Properties()));

    public static final RegistryObject<Item> CHESTGUI_RESET = REGISTRY.register("chestgui_reset", ()->new Item(new Item.Properties()));

    public static final RegistryObject<Item> CHESTGUI_FORWARD = REGISTRY.register("chestgui_forward", ()->new Item(new Item.Properties()));


    public static void register(IEventBus bus)
    {
        REGISTRY.register(bus);
    }
}
