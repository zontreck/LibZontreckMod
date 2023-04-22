package dev.zontreck.libzontreck.currency;

import com.google.common.collect.Lists;
import dev.zontreck.ariaslib.events.Event;
import dev.zontreck.ariaslib.events.EventBus;
import dev.zontreck.ariaslib.events.annotations.Subscribe;
import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.chat.ChatColorFactory;
import dev.zontreck.libzontreck.currency.events.BankAccountCreatedEvent;
import dev.zontreck.libzontreck.currency.events.BankReadyEvent;
import dev.zontreck.libzontreck.currency.events.TransactionEvent;
import dev.zontreck.libzontreck.currency.events.WalletUpdatedEvent;
import dev.zontreck.libzontreck.exceptions.InvalidSideException;
import dev.zontreck.libzontreck.networking.ModMessages;
import dev.zontreck.libzontreck.networking.packets.S2CWalletUpdatedPacket;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.util.ServerUtilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Bank
{
	public static final Path BANK_DATA;

	static {
		BANK_DATA = LibZontreck.BASE_CONFIG.resolve("bank.nbt");
	}

	private Bank(){
		load();
	}

	/**
	 * Internal function to deserialize NBT
	 */
	private void load()
	{
		try {
			CompoundTag data = NbtIo.read(BANK_DATA.toFile());
			accounts=new ArrayList<>();
			ListTag acts = data.getList("accounts", Tag.TAG_COMPOUND);
			for(Tag t : acts)
			{
				accounts.add(new Account((CompoundTag) t));
			}

			EventBus.BUS.post(new BankReadyEvent());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Do not use manually, this saves the bank data to disk. This is fired automatically when transactions are posted to accounts.
	 */
	public void commit()
	{
		CompoundTag tag = new CompoundTag();
		ListTag lst = new ListTag();
		for(Account act : accounts)
		{
			lst.add(act.save());
		}
		tag.put("accounts", lst);

		try {
			NbtIo.write(tag, BANK_DATA.toFile());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static final Bank instance = new Bank();

	public List<Account> accounts = new ArrayList<>();

	public static Account getAccount(UUID ID)
	{
		if(!hasAccount(ID))return null;
		return instance.accounts.stream().filter(c->c.player_id.equals(ID)).collect(Collectors.toList()).get(0);
	}

	public static boolean hasAccount(UUID ID)
	{
		return instance.accounts.stream().filter(c->c.player_id.equals(ID)).collect(Collectors.toList()).stream().count()>0;
	}

	public static void makeAccount(UUID ID)
	{
		if(!hasAccount(ID)){
			instance.accounts.add(new Account(ID));

			instance.commit();
			EventBus.BUS.post(new BankAccountCreatedEvent(getAccount(ID)));
		}else return;
	}

	/**
	 * Attempts to post a transaction and perform the money transfer
	 *
	 * Please post your reason to the Reasons list when cancelling.
	 * @param tx The transaction being attempted
	 * @return True if the transaction has been accepted. False if the transaction was rejected, or insufficient funds.
	 */
	public static boolean postTx(Transaction tx) throws InvalidSideException {
		if(ServerUtilities.isClient())return false;
		TransactionEvent ev = new TransactionEvent(tx);
		if(EventBus.BUS.post(ev))
		{
			// Send the list of reasons to the user
			String reasonStr = String.join("\n", ev.reasons);

			Account from = ev.tx.from.get();
			Account to = ev.tx.to.get();

			if(from.isValidPlayer())
			{
				ChatHelpers.broadcastTo(from.player_id, ChatHelpers.macro("!Dark_Gray![!Dark_Blue!Bank!Dark_Gray!] !Dark_Red!The transaction could not be completed because of the following reasons: " + reasonStr), LibZontreck.THE_SERVER);
			}
			if(to.isValidPlayer())
			{
				ChatHelpers.broadcastTo(to.player_id, ChatHelpers.macro("!Dark_Gray![!Dark_Blue!Bank!Dark_Gray!] !Dark_Red!The transaction could not be completed because of the following reasons: " + reasonStr), LibZontreck.THE_SERVER);
			}

			return false;
		}else {
			// Tx accepted
			// Process funds now
			Account from = ev.tx.from.get();
			Account to = ev.tx.to.get();
			int fromOld = from.balance;
			int toOld = to.balance;
			from.balance -= tx.amount;
			to.balance += tx.amount;

			from.history.add(tx);
			to.history.add(tx);

			Profile toProf = null;
			Profile fromProf = null;
			try{
				fromProf = Profile.get_profile_of(from.player_id.toString());
			}catch(UserProfileNotYetExistsException e){
				e.printStackTrace();
			}
			try {
				toProf = Profile.get_profile_of(to.player_id.toString());
			} catch (UserProfileNotYetExistsException e) {
				e.printStackTrace();
			}

			if(!from.isValidPlayer())
			{
				fromProf = Profile.SYSTEM;
			}

			if(!to.isValidPlayer())
			{
				toProf = Profile.SYSTEM;
			}

			if(from.isValidPlayer())
				ChatHelpers.broadcastTo(from.player_id, ChatHelpers.macro("!Dark_Gray![!Dark_Blue!Bank!Dark_Gray!] !Dark_Green!You sent !White!${0} !Dark_green!to {1}", String.valueOf(tx.amount), toProf.name_color+toProf.nickname), LibZontreck.THE_SERVER);

			if(to.isValidPlayer())
				ChatHelpers.broadcastTo(from.player_id, ChatHelpers.macro("!Dark_Gray![!Dark_Blue!Bank!Dark_Gray!] {0} !Dark_Green!paid you ${1}", String.valueOf(tx.amount), toProf.name_color+toProf.nickname), LibZontreck.THE_SERVER);

			if(to.isValidPlayer() && ServerUtilities.playerIsOffline(to.player_id)) Profile.unload(toProf);
			if(from.isValidPlayer() && ServerUtilities.playerIsOffline(from.player_id))
				Profile.unload(fromProf);


			EventBus.BUS.post(new WalletUpdatedEvent(from.player_id, fromOld, from.balance, tx));
			EventBus.BUS.post(new WalletUpdatedEvent(to.player_id, toOld, to.balance, tx));

			if(from.isValidPlayer() && !ServerUtilities.playerIsOffline(from.player_id))
			{
				ModMessages.sendToPlayer(new S2CWalletUpdatedPacket(from.player_id, tx, from.balance, fromOld), ServerUtilities.getPlayerByID(from.player_id.toString()));
			}
			if(to.isValidPlayer() && !ServerUtilities.playerIsOffline(to.player_id))
			{
				ModMessages.sendToPlayer(new S2CWalletUpdatedPacket(to.player_id, tx, to.balance, toOld), ServerUtilities.getPlayerByID(to.player_id.toString()));
			}

			instance.commit();

		}
		return true;

	}

	/**
	 * This event is fired when wallets get updated. It cannot be cancelled
	 * @param ev The event containing the player ID and new+old wallet data
	 */
	@Subscribe
	public static void onWalletUpdate(WalletUpdatedEvent ev)
	{

	}
}
