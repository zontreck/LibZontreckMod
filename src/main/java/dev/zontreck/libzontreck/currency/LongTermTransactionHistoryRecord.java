package dev.zontreck.libzontreck.currency;

import dev.zontreck.libzontreck.LibZontreck;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LongTermTransactionHistoryRecord
{
	public static final Path BASE;
	static{
		BASE = LibZontreck.BASE_CONFIG.resolve("transaction_history");
		if(!BASE.toFile().exists())
		{
			BASE.toFile().mkdir();
		}
	}
	public static Path ofPath(String name)
	{
		return BASE.resolve(name);
	}
	public void commit()
	{
		ListTag txs = new ListTag();
		for(Transaction tx : history)
		{
			txs.add(tx.save());
		}
		CompoundTag tag = new CompoundTag();
		tag.put("history", txs);
		try {
			NbtIo.write(tag, ofPath(player_id.toString()+".nbt").toFile());

		}catch(IOException e){}
	}
	public UUID player_id;

	public List<Transaction> history;
	private LongTermTransactionHistoryRecord(UUID ID)
	{
		player_id=ID;
		try {
			CompoundTag tag = NbtIo.read(ofPath(ID.toString()+".nbt").toFile());
			ListTag hist = tag.getList("history", Tag.TAG_COMPOUND);
			history = new ArrayList<>();
			for(Tag t : hist){
				history.add(new Transaction((CompoundTag) t));
			}
		} catch (IOException e) {
			history = new ArrayList<>();
		}
	}

	public void addHistory(List<Transaction> other)
	{
		for (Transaction tx : other)
		{
			history.add(tx);
		}
	}
	public static LongTermTransactionHistoryRecord of(UUID ID)
	{
		return new LongTermTransactionHistoryRecord(ID);
	}
}
