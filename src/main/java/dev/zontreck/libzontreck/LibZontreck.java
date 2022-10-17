package dev.zontreck.libzontreck;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LibZontreck.MOD_ID)
public class LibZontreck {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "libzontreck";
    public static MinecraftServer THE_SERVER;

    public LibZontreck(){
        
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addListener(this::setup);

        
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    }

    
    @SubscribeEvent
    public void onServerStarted(final ServerStartedEvent event)
    {
        THE_SERVER = event.getServer();
    }
}
