package dev.zontreck.libzontreck.commands;

import com.mojang.brigadier.CommandDispatcher;


import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;

public class CreditsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatch)
    {
        dispatch.register(Commands.literal("credits_ariasmods").executes(s->credits(s.getSource())));
    }

    private static int credits(CommandSourceStack source) {
        // Open the credits GUI
        if(source.getEntity() instanceof Player)
        {
            // OK. 
            
            

            return 0;
        }else return 1;
    }

    
}
