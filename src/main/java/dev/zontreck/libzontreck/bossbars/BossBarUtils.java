package dev.zontreck.libzontreck.bossbars;

import net.minecraft.client.Minecraft;
import net.minecraft.server.bossevents.CustomBossEvents;
import net.minecraft.server.commands.BossBarCommands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class BossBarUtils {
    public class BossBarStructure
    {
        public String Header;
        public int Percent;
    }
    public static void sendBossBarToPlayer(Player p, BossBarStructure struc)
    {
        if(Minecraft.getInstance().level.isClientSide())return;

        ServerPlayer sp = (ServerPlayer)p;
        if(sp==null)return;

        //sp.server.getCustomBossEvents().create(null, null)
    }
}
