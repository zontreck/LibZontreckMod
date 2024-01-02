package dev.zontreck.libzontreck.events;

import dev.zontreck.ariaslib.terminal.Task;
import dev.zontreck.ariaslib.util.DelayedExecutorService;
import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.exceptions.InvalidSideException;
import dev.zontreck.libzontreck.memory.PlayerContainer;
import dev.zontreck.libzontreck.networking.ModMessages;
import dev.zontreck.libzontreck.networking.packets.S2CWalletInitialSyncPacket;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import dev.zontreck.libzontreck.util.ServerUtilities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LibZontreck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandlers {
    
    @SubscribeEvent
    public void onPlayerTick(LivingEvent.LivingTickEvent ev)
    {
        if(ev.getEntity().level().isClientSide) return;

        if(ev.getEntity() instanceof ServerPlayer player)
        {
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
    public void onPlayerJoin(final PlayerEvent.PlayerLoggedInEvent ev)
    {
        if(ev.getEntity().level().isClientSide)return;

        ServerPlayer player = (ServerPlayer)ev.getEntity();
        Profile prof = Profile.factory(player);
        ServerLevel level = player.serverLevel();

        MinecraftForge.EVENT_BUS.post(new ProfileLoadedEvent(prof, player, level));

        DelayedExecutorService.getInstance().schedule(new Task("send-msg", true) {
            @Override
            public void run() {
                // Check player wallet, then send wallet to client
                ModMessages.sendToPlayer(new S2CWalletInitialSyncPacket(player.getUUID()), player);
            }
        }, 10);
    }

    @SubscribeEvent
    public void onLeave(final PlayerEvent.PlayerLoggedOutEvent ev)
    {
        if(ev.getEntity().level().isClientSide)return;
        // Get player profile, send disconnect alert, then commit profile and remove it from memory

        try {
            if(ServerUtilities.playerIsOffline(ev.getEntity().getUUID()))
            {

                Profile px=null;
                try {
                    px = Profile.get_profile_of(ev.getEntity().getStringUUID());
                } catch (UserProfileNotYetExistsException e) {
                    e.printStackTrace();
                }
                Profile.unload(px);
            }
        } catch (InvalidSideException e) {
            throw new RuntimeException(e);
        }

    }
}
