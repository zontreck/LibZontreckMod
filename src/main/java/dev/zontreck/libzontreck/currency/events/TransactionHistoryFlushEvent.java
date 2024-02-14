package dev.zontreck.libzontreck.currency.events;


import dev.zontreck.libzontreck.currency.Account;
import dev.zontreck.libzontreck.currency.LongTermTransactionHistoryRecord;
import dev.zontreck.libzontreck.currency.Transaction;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class TransactionHistoryFlushEvent extends Event
{
	public LongTermTransactionHistoryRecord txHistory;
	public Account associatedAccount;
	public List<Transaction> flushed;

	public TransactionHistoryFlushEvent(Account act, LongTermTransactionHistoryRecord txHistory, List<Transaction> flushed)
	{
		associatedAccount=act;
		this.txHistory=txHistory;
		this.flushed=flushed;
	}
}
