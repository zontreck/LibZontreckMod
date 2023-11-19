package dev.zontreck.libzontreck.currency.events;

import dev.zontreck.eventsbus.Event;
import dev.zontreck.libzontreck.currency.Transaction;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

/**
 * This event is dispatched on both the Client and the Server
 */
public class WalletUpdatedEvent extends Event
{
	public int newBal;
	public int oldBal;
	public UUID player;
	public Transaction tx;

	public WalletUpdatedEvent(UUID player, int old, int newBal, Transaction tx)
	{
		this.player = player;
		this.oldBal = old;
		this.newBal = newBal;
		this.tx=tx;
	}
}
