package dev.zontreck.libzontreck.currency;


import dev.zontreck.eventsbus.Bus;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.currency.events.TransactionHistoryFlushEvent;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Account
{
	public UUID player_id;
	public List<Transaction> history;
	public int balance;

	public String accountName = "";
	public boolean isPlayer=true;

	public AccountReference getRef()
	{
		return new AccountReference(player_id);
	}

	protected Account(UUID ID)
	{
		player_id=ID;
		history=new ArrayList<>();
		balance=0;
		if(isValidPlayer()) {
			try {
				Profile prof = Profile.get_profile_of(ID.toString());
				accountName = prof.name_color + prof.nickname;
				isPlayer=true;
			} catch (UserProfileNotYetExistsException e) {
				accountName = ChatColor.doColors("!Dark_Red!SYSTEM!White!");
				isPlayer=false;
			}

		} else {
			accountName = ChatColor.doColors("!Dark_Red!SYSTEM!White!");
			isPlayer=false;
		}
	}

	public CompoundTag save()
	{
		CompoundTag tag = new CompoundTag();
		tag.put("id", NbtUtils.createUUID(player_id));
		tag.putInt("balance", balance);
		ListTag txs = new ListTag();
		for(Transaction tx : history)
		{
			txs.add(tx.save());
		}

		tag.put("history", txs);
		tag.putString("name", accountName);
		tag.putBoolean("player", isPlayer);

		return tag;
	}

	public Account(CompoundTag tag)
	{
		player_id = NbtUtils.loadUUID(tag.get("id"));
		balance = tag.getInt("balance");
		accountName = tag.getString("name");
		isPlayer = tag.getBoolean("player");
		history=new ArrayList<>();
		ListTag lst = tag.getList("history", Tag.TAG_COMPOUND);
		for(Tag t : lst){
			CompoundTag lTag = (CompoundTag) t;
			history.add(new Transaction(lTag));
		}
	}

	/**
	 * Internal function for use only by the garbage collector to reduce memory footprint. Maximum of 20 transactions will be retained in the memory
	 *
	 * When the TX history grows beyond 20, the history should clear and it should get transferred to the long-term tx history storage. That file is not retained in memory, and only gets loaded when clearing to merge the lists. It is immediately saved and unloaded
	 * @see LongTermTransactionHistoryRecord
	 */
	public void flushTxHistory() {
		LongTermTransactionHistoryRecord rec = LongTermTransactionHistoryRecord.of(player_id);
		rec.addHistory(history);
		rec.commit();
		Bus.Post(new TransactionHistoryFlushEvent(this, rec, history));
		rec = null;
		history = new ArrayList<>();
	}

	/**
	 * Checks if the account is a player or system account
	 * @return True if the player has a valid UUID
	 */
	public boolean isValidPlayer()
	{
		return isPlayer;
	}
}
