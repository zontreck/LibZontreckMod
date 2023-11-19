package dev.zontreck.libzontreck.networking.packets;

import dev.zontreck.eventsbus.Bus;
import dev.zontreck.libzontreck.currency.Transaction;
import dev.zontreck.libzontreck.currency.events.WalletUpdatedEvent;
import dev.zontreck.libzontreck.util.ServerUtilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.function.Supplier;

public class S2CWalletUpdatedPacket implements  IPacket
{
	public UUID ID;
	public Transaction tx;
	public int balance;
	public int oldBal;

	public S2CWalletUpdatedPacket(FriendlyByteBuf buf)
	{
		ID = buf.readUUID();
		tx = new Transaction(buf.readNbt());
		balance = buf.readInt();
		oldBal = buf.readInt();
	}

	public S2CWalletUpdatedPacket(UUID ID, Transaction tx, int bal, int old)
	{
		this.ID= ID;
		this.tx=tx;
		this.balance=bal;
		oldBal=old;
	}

	public S2CWalletUpdatedPacket(){}


	@Override
	public void deserialize(CompoundTag data) {

	}

	@Override
	public void serialize(CompoundTag data) {

	}

	@Override
	public void toBytes(FriendlyByteBuf buf) {
		buf.writeUUID(ID);
		buf.writeNbt(tx.save());
		buf.writeInt(balance);
		buf.writeInt(oldBal);
	}

	@Override
	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		return ServerUtilities.handlePacket(supplier, new Runnable() {
			@Override
			public void run() {
				Bus.Post(new WalletUpdatedEvent(ID, oldBal, balance, tx));

			}
		});
	}

	@Override
	public NetworkDirection getDirection() {
		return NetworkDirection.PLAY_TO_CLIENT;
	}

	@Override
	public void register(SimpleChannel chan) {
		ServerUtilities.registerPacket(chan, S2CWalletUpdatedPacket.class, this, S2CWalletUpdatedPacket::new);
	}
}
