package dev.zontreck.libzontreck.commands;

import dev.zontreck.libzontreck.LibZontreck;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=LibZontreck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class Commands {
    @SubscribeEvent
    public void onCommandsRegister(RegisterCommandsEvent ev)
    {
        CreditsCommand.register(ev.getDispatcher());
        //GetHead.register(ev.getDispatcher());
    }
}
