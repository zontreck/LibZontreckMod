package dev.zontreck.libzontreck.currency.events;


import dev.zontreck.eventsbus.Cancellable;
import dev.zontreck.eventsbus.Event;
import dev.zontreck.libzontreck.currency.Transaction;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

@Cancellable
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
