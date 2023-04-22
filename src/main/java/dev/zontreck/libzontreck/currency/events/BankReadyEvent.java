package dev.zontreck.libzontreck.currency.events;

import dev.zontreck.ariaslib.events.Event;

/**
 * Contains no information by itself, it only signals that the Bank is open for business
 *
 * @see dev.zontreck.libzontreck.currency.Bank
 */
public class BankReadyEvent extends Event
{

	@Override
	public boolean isCancellable() {
		return false;
	}
}
