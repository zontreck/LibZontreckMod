package dev.zontreck.libzontreck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import dev.zontreck.libzontreck.events.PlayerChangedPositionEvent;
import dev.zontreck.libzontreck.events.ProfileLoadedEvent;
import dev.zontreck.libzontreck.memory.PlayerContainer;
import dev.zontreck.libzontreck.memory.VolatilePlayerStorage;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.util.DelayedExecutorService;
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

    static{
        PROFILES = new HashMap<>();
    }

    public LibZontreck(){
        LibZontreck.playerStorage=new VolatilePlayerStorage();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addListener(this::setup);
        
        bus.register(DelayedExecutorService.getInstance());
        
        MinecraftForge.EVENT_BUS.register(this);
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
    }


    @Mod.EventBusSubscriber(modid = LibZontreck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEventBus
    {
        @SubscribeEvent
        public void onPlayerTick(LivingUpdateEvent ev)
        {
            if(ev.getEntity().level.isClientSide)return;

            if(ev.getEntity() instanceof ServerPlayer)
            {
                ServerPlayer player = (ServerPlayer)ev.getEntity();
                PlayerContainer cont = LibZontreck.playerStorage.get(player.getUUID());
                
                if(cont.player.positionChanged())
                {
                    cont.player.update();

                    PlayerChangedPositionEvent pcpe = new PlayerChangedPositionEvent(player, cont.player.position, cont.player.lastPosition);
                    MinecraftForge.EVENT_BUS.post(pcpe);
                }
            }
        }


        @SubscribeEvent
        public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent ev)
        {
            if(ev.getEntity().level.isClientSide)return;

            ServerPlayer player = (ServerPlayer)ev.getPlayer();
            Profile prof = Profile.factory(player);

            MinecraftForge.EVENT_BUS.post(new ProfileLoadedEvent(prof));
        }
    }
}
