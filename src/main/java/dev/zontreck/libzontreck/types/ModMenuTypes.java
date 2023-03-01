package dev.zontreck.libzontreck.types;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.dynamicchest.ChestGuiMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.CONTAINERS, LibZontreck.MOD_ID);

    public static final RegistryObject<MenuType<ChestGuiMenu>> CHESTGUI = REGISTER.register("dynchest", ()->new MenuType<>(ChestGuiMenu::new));

    public static void register(IEventBus bus)
    {
        REGISTER.register(bus);
    }
}
