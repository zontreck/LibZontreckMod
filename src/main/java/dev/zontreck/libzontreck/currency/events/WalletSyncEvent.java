package dev.zontreck.libzontreck.currency.events;

import dev.zontreck.libzontreck.currency.Account;
import dev.zontreck.libzontreck.currency.Bank;
import net.minecraftforge.eventbus.api.Event;

import java.util.UUID;

/**
 * This event is not cancellable!!
 */
public class WalletSyncEvent extends Event
{
	public Account walletInformation;

	public WalletSyncEvent(UUID player)
	{
		walletInformation = Bank.getAccount(player);
	}

	public WalletSyncEvent(Account act)
	{
		walletInformation = act;
	}
}
