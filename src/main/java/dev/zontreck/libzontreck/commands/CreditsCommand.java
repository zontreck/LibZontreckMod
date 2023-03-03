package dev.zontreck.libzontreck.commands;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;

import dev.zontreck.libzontreck.dynamicchest.ChestGuiContainer;
import dev.zontreck.libzontreck.util.heads.CreditsEntry;
import dev.zontreck.libzontreck.util.heads.HeadCache;
import dev.zontreck.libzontreck.util.heads.HeadUtilities;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlayerHeadItem;

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
