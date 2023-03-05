package dev.zontreck.libzontreck.events;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.memory.PlayerContainer;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LibZontreck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandlers {
    
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
    public void onPlayerJoin(final PlayerEvent.PlayerLoggedInEvent ev)
    {
        if(ev.getEntity().level.isClientSide)return;

        ServerPlayer player = (ServerPlayer)ev.getPlayer();
        Profile prof = Profile.factory(player);
        ServerLevel level = player.getLevel();

        MinecraftForge.EVENT_BUS.post(new ProfileLoadedEvent(prof, player, level));
    }

    @SubscribeEvent
    public void onLeave(final PlayerEvent.PlayerLoggedOutEvent ev)
    {
        if(ev.getEntity().level.isClientSide)return;
        // Get player profile, send disconnect alert, then commit profile and remove it from memory
        Profile px=null;
        try {
            px = Profile.get_profile_of(ev.getEntity().getStringUUID());
        } catch (UserProfileNotYetExistsException e) {
            e.printStackTrace();
        }
        Profile.unload(px);
    }
}
