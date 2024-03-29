package dev.zontreck.libzontreck;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import dev.zontreck.ariaslib.util.DelayedExecutorService;
import dev.zontreck.eventsbus.Bus;
import dev.zontreck.libzontreck.chestgui.ChestGUIRegistry;
import dev.zontreck.libzontreck.currency.Bank;
import dev.zontreck.libzontreck.currency.CurrencyHelper;
import dev.zontreck.libzontreck.items.ModItems;
import dev.zontreck.libzontreck.menus.ChestGUIScreen;
import dev.zontreck.libzontreck.types.ModMenuTypes;
import dev.zontreck.libzontreck.networking.NetworkEvents;
import net.minecraft.client.gui.screens.MenuScreens;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import dev.zontreck.libzontreck.commands.Commands;
import dev.zontreck.libzontreck.events.ForgeEventHandlers;
import dev.zontreck.libzontreck.memory.VolatilePlayerStorage;
import dev.zontreck.libzontreck.networking.ModMessages;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.util.FileTreeDatastore;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LibZontreck.MOD_ID)
public class LibZontreck {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "libzontreck";
    public static final Map<String, Profile> PROFILES;
    public static MinecraftServer THE_SERVER;
    public static VolatilePlayerStorage playerStorage;
    public static boolean ALIVE=true;
    public static final String FILESTORE = FileTreeDatastore.get();
    public static final Path BASE_CONFIG;
    public static final String PLAYER_INFO_URL = "https://api.mojang.com/users/profiles/minecraft/";
	public static final String PLAYER_SKIN_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
    public static final UUID NULL_ID;

    public static boolean LIBZONTRECK_SERVER_AVAILABLE=false;


    public static LogicalSide CURRENT_SIDE;



    static{
        NULL_ID = new UUID(0,0);
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
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
        MinecraftForge.EVENT_BUS.register(new Commands());
        MinecraftForge.EVENT_BUS.register(new NetworkEvents());
        MinecraftForge.EVENT_BUS.register(ChestGUIRegistry.class);

        Bus.Reset();

        ModMenuTypes.REGISTRY.register(bus);
        //CreativeModeTabs.register(bus);
        ModItems.register(bus);

        Bus.Register(CurrencyHelper.class, null);
        Bus.Register(Bank.class, null);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        ModMessages.register();
    }

    
    @SubscribeEvent
    public void onServerStarted(final ServerStartedEvent event)
    {
        THE_SERVER = event.getServer();
        ALIVE=true;
        CURRENT_SIDE = LogicalSide.SERVER;
    }

    @SubscribeEvent
    public void onServerStopping(final ServerStoppingEvent ev)
    {
        ALIVE=false;
        DelayedExecutorService.stop();

        Iterator<Profile> iProfile = PROFILES.values().iterator();
        while(iProfile.hasNext())
        {
            Profile prof = iProfile.next();
            iProfile.remove();
            prof=null;
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent ev) {
            LibZontreck.CURRENT_SIDE = LogicalSide.CLIENT;
            LibZontreck.ALIVE = false; // Prevents loops on the client that are meant for server tick processing


            MenuScreens.register(ModMenuTypes.CHEST_GUI_MENU.get(), ChestGUIScreen::new);
        }
    }

}
