package dev.zontreck.libzontreck.currency;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

import java.util.Date;

public class Transaction
{
	public AccountReference from;
	public AccountReference to;
	public int amount;
	public long timestamp;


	public CompoundTag save()
	{
		CompoundTag tag = new CompoundTag();
		tag.put("from", NbtUtils.createUUID(from.id));
		tag.put("to", NbtUtils.createUUID(to.id));
		tag.putInt("amount", amount);
		tag.putLong("timestamp", timestamp);

		return tag;
	}

	public Transaction(CompoundTag tag)
	{
		from = new AccountReference(NbtUtils.loadUUID(tag.get("from")));
		to = new AccountReference(NbtUtils.loadUUID(tag.get("to")));
		amount = tag.getInt("amount");
		timestamp = tag.getLong("timestamp");
	}

	public Transaction(Account from, Account to, int amount, long ts)
	{
		this.from = from.getRef();
		this.to = to.getRef();
		this.amount=amount;
		timestamp = ts;
	}
}