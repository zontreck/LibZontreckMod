package dev.zontreck.libzontreck.types;

import dev.zontreck.libzontreck.LibZontreck;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModMenuTypes {
    public static DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.CONTAINERS, LibZontreck.MOD_ID);

    public static void register(IEventBus bus)
    {
        REGISTER.register(bus);
    }
}
