package dev.zontreck.libzontreck.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.zontreck.libzontreck.util.HeadUtilities;
import dev.zontreck.libzontreck.util.HeadCache.HeadCacheItem;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlayerHeadItem;

public class GetHead {
    public static void register(CommandDispatcher<CommandSourceStack> dispatch)
    {
        dispatch.register(Commands.literal("aria_debug_get_head").executes(c-> getHead(c.getSource(), c.getSource().getEntity().getName().getContents())).then(Commands.argument("name", StringArgumentType.string()).executes(c -> getHead(c.getSource(), StringArgumentType.getString(c, "name")))));
        
    }

    private static int getHead(CommandSourceStack source, String string) {
        try {
            ServerPlayer player= source.getPlayerOrException();
            ItemStack head = HeadUtilities.get(string);

            player.addItem(head);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        

        return 0;
    }
    
}
