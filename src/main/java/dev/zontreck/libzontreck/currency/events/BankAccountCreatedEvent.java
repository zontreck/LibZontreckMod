package dev.zontreck.libzontreck.currency.events;


import dev.zontreck.libzontreck.currency.Account;
import net.minecraftforge.eventbus.api.Event;

public class BankAccountCreatedEvent extends Event
{
	public Account account;
	public BankAccountCreatedEvent(Account act)
	{
		account=act;
	}
}
