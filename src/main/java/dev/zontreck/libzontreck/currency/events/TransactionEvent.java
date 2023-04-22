package dev.zontreck.libzontreck.currency.events;

import dev.zontreck.ariaslib.events.Event;
import dev.zontreck.libzontreck.currency.Transaction;

import java.util.List;

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

	@Override
	public boolean isCancellable() {
		return true;
	}
}
