package dev.zontreck.libzontreck.currency.events;


import dev.zontreck.ariaslib.events.Event;
import dev.zontreck.libzontreck.currency.Account;

public class BankAccountCreatedEvent extends Event
{
	public Account account;
	public BankAccountCreatedEvent(Account act)
	{
		account=act;
	}

	@Override
	public boolean isCancellable() {
		return false;
	}
}
