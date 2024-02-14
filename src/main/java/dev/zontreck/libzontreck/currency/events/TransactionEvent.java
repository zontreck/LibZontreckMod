package dev.zontreck.libzontreck.currency.events;


import dev.zontreck.libzontreck.currency.Transaction;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

@Cancelable
public class TransactionEvent extends Event
{
	public Transaction tx;

	/**
	 * This is the list of reasons why a transaction was aborted or blocked
	 */
	public List<String> reasons;

	public TransactionEvent(Transaction txNew)
	{
		tx=txNew;
	}
}
