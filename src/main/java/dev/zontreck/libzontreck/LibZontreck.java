package dev.zontreck.libzontreck;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import dev.zontreck.libzontreck.events.ForgeEventHandlers;
import dev.zontreck.libzontreck.events.PlayerChangedPositionEvent;
import dev.zontreck.libzontreck.events.ProfileLoadedEvent;
import dev.zontreck.libzontreck.memory.PlayerContainer;
import dev.zontreck.libzontreck.memory.VolatilePlayerStorage;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.util.DelayedExecutorService;
import dev.zontreck.libzontreck.util.FileTreeDatastore;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LibZontreck.MOD_ID)
public class LibZontreck {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "libzontreck";
    public static final Map<String, Profile> PROFILES;
    public static MinecraftServer THE_SERVER;
    public static VolatilePlayerStorage playerStorage;
    public static boolean ALIVE;
    public static final String FILESTORE = FileTreeDatastore.get();
    public static final Path BASE_CONFIG;

    static{
        PROFILES = new HashMap<>();
        BASE_CONFIG = FileTreeDatastore.of("libzontreck");

        if(!BASE_CONFIG.toFile().exists())
        {
            try {
                Files.createDirectory(BASE_CONFIG);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public LibZontreck(){
        LibZontreck.playerStorage=new VolatilePlayerStorage();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addListener(this::setup);
        
        
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(DelayedExecutorService.getInstance());
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    }

    
    @SubscribeEvent
    public void onServerStarted(final ServerStartedEvent event)
    {
        THE_SERVER = event.getServer();
        ALIVE=true;
    }

    @SubscribeEvent
    public void onServerStopping(final ServerStoppingEvent ev)
    {
        ALIVE=false;

        Iterator<Profile> iProfile = PROFILES.values().iterator();
        while(iProfile.hasNext())
        {
            Profile prof = iProfile.next();
            iProfile.remove();
            prof=null;
        }
    }

}
