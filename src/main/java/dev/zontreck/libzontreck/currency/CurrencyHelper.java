package dev.zontreck.libzontreck.currency;

import dev.zontreck.ariaslib.events.annotations.Subscribe;
import dev.zontreck.libzontreck.currency.events.CurrencyBalanceCheckEvent;

import java.util.List;

public class CurrencyHelper {
	@Subscribe
	public static void onCurrencyBalanceCheck(CurrencyBalanceCheckEvent ev)
	{

	}
}
