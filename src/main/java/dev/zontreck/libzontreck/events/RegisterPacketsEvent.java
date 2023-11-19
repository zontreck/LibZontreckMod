package dev.zontreck.libzontreck.events;

import java.util.ArrayList;
import java.util.List;

import dev.zontreck.libzontreck.networking.packets.IPacket;
import net.minecraftforge.eventbus.api.Event;

/**
 * Used to register your packets with LibZontreck. Packets must extend IPacket and implement PacketSerializable. This is dispatched on both logical sides, and is considered a final event. It is not cancelable
 * @see IPacket
 */
public class RegisterPacketsEvent extends Event
{
    public final List<IPacket> packets = new ArrayList<>();
}
