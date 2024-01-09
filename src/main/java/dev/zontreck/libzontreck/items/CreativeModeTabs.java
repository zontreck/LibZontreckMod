package dev.zontreck.libzontreck.items;

import dev.zontreck.libzontreck.LibZontreck;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

//@Mod.EventBusSubscriber(modid = LibZontreck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeModeTabs
{
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LibZontreck.MOD_ID);

    public static final List<Supplier<? extends ItemLike>> LZ_MOD_ITEMS = new ArrayList<>();

    public static final RegistryObject<CreativeModeTab> LIBZONTRECK_TAB = REGISTRY.register("libzontreck", ()->CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.tabs.libzontreck"))
            .icon(Items.BARRIER::getDefaultInstance)
            .displayItems((display,output)->LZ_MOD_ITEMS.forEach(it->output.accept(it.get())))
            .build()
    );

    public static <T extends Item> RegistryObject<T> addToLZTab(RegistryObject<T> item)
    {
        LZ_MOD_ITEMS.add(item);
        return item;
    }


    public static void register(IEventBus bus)
    {
        REGISTRY.register(bus);
    }

}
