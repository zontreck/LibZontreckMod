package dev.zontreck.libzontreck.networking;

import dev.zontreck.libzontreck.events.RegisterPacketsEvent;
import dev.zontreck.libzontreck.networking.packets.S2CPlaySoundPacket;
import dev.zontreck.libzontreck.networking.packets.S2CWalletInitialSyncPacket;
import dev.zontreck.libzontreck.networking.packets.S2CWalletUpdatedPacket;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NetworkEvents
{
	@SubscribeEvent
	public void onRegisterPackets(RegisterPacketsEvent ev)
	{
		ev.packets.add(new S2CWalletUpdatedPacket());
		ev.packets.add(new S2CPlaySoundPacket());
		ev.packets.add(new S2CWalletInitialSyncPacket());
	}
}
