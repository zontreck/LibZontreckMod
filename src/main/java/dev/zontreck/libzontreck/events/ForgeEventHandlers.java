package dev.zontreck.libzontreck.events;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.memory.PlayerContainer;
import dev.zontreck.libzontreck.profiles.Profile;
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
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent ev)
    {
        if(ev.getEntity().level.isClientSide)return;

        ServerPlayer player = (ServerPlayer)ev.getPlayer();
        Profile prof = Profile.factory(player);

        MinecraftForge.EVENT_BUS.post(new ProfileLoadedEvent(prof));
    }

}
