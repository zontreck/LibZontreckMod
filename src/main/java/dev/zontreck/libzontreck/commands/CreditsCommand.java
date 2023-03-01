package dev.zontreck.libzontreck.commands;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.PlayerHeadItem;

public class CreditsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatch)
    {
        dispatch.register(Commands.literal("aria_credits").executes(s->credits(s.getSource())));
    }

    private static int credits(CommandSourceStack source) {
        // Open the credits GUI
        if(source.getEntity() instanceof Player)
        {
            // OK. 
            ServerPlayer play = (ServerPlayer)source.getEntity();
            

            return 0;
        }else return 1;
    }

    
}
