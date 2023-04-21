package dev.zontreck.libzontreck.currency.events;

import dev.zontreck.ariaslib.events.Event;
import net.minecraft.world.entity.player.Player;


public class CurrencyBalanceCheckEvent extends Event
{
	public final Player player;
	public String balance;

	public CurrencyBalanceCheckEvent(Player player)
	{
		this.player=player;
	}
	@Override
	public boolean isCancellable() {
		return false;
	}
}
