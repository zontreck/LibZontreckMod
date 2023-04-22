package dev.zontreck.libzontreck.networking.packets;

import dev.zontreck.libzontreck.currency.Account;
import dev.zontreck.libzontreck.currency.Bank;
import dev.zontreck.libzontreck.currency.events.WalletSyncEvent;
import dev.zontreck.libzontreck.util.ServerUtilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.UUID;
import java.util.function.Supplier;

public class S2CWalletInitialSyncPacket implements IPacket
{
	public Account act;
	public S2CWalletInitialSyncPacket(FriendlyByteBuf buf)
	{
		act = new Account(buf.readNbt());
	}
	public S2CWalletInitialSyncPacket(Account act)
	{
		this.act=act;
	}
	public S2CWalletInitialSyncPacket(UUID ID)
	{
		this.act= Bank.getAccount(ID);
	}
	public S2CWalletInitialSyncPacket(){}

	@Override
	public void deserialize(CompoundTag data) {

	}

	@Override
	public void serialize(CompoundTag data) {

	}

	@Override
	public void toBytes(FriendlyByteBuf buf) {
		buf.writeNbt(act.save());
	}

	@Override
	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		return ServerUtilities.handlePacket(supplier, new Runnable() {
			@Override
			public void run() {
				MinecraftForge.EVENT_BUS.post(new WalletSyncEvent(act));

			}
		});
	}

	@Override
	public NetworkDirection getDirection() {
		return NetworkDirection.PLAY_TO_CLIENT;
	}

	@Override
	public void register(SimpleChannel chan) {
		ServerUtilities.registerPacket(chan, S2CWalletInitialSyncPacket.class, this, S2CWalletInitialSyncPacket::new);
	}
}
