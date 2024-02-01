package dev.zontreck.libzontreck.types;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.menus.ChestGUIMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes
{
    public static DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, LibZontreck.MOD_ID);

    public static RegistryObject<MenuType<ChestGUIMenu>> CHEST_GUI_MENU = registerMenuType(ChestGUIMenu::new, "chestgui");

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name)
    {
        return REGISTRY.register(name, ()-> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus bus)
    {
        REGISTRY.register(bus);
    }
}
