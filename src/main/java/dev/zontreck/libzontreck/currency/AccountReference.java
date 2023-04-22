package dev.zontreck.libzontreck.currency;

import java.util.UUID;

public class AccountReference
{
	public UUID id;
	public Account get()
	{
		return Bank.getAccount(id);
	}

	protected AccountReference(UUID ID)
	{
		id=ID;
	}
}
