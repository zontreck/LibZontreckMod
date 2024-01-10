package dev.zontreck.libzontreck.commands;

import com.mojang.brigadier.CommandDispatcher;


import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.chestgui.ChestGUI;
import dev.zontreck.libzontreck.chestgui.ChestGUIButton;
import dev.zontreck.libzontreck.chestgui.ChestGUIIdentifier;
import dev.zontreck.libzontreck.networking.ModMessages;
import dev.zontreck.libzontreck.networking.packets.S2CCloseChestGUI;
import dev.zontreck.libzontreck.util.heads.CreditsEntry;
import dev.zontreck.libzontreck.util.heads.HeadCache;
import dev.zontreck.libzontreck.vectors.Vector2i;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
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
            ChestGUI gui = ChestGUI.builder().withGUIId(new ChestGUIIdentifier("creditsgui")).withPlayer(source.getEntity().getUUID()).withTitle("Aria's Mods - Credits");

            int x = 0;
            int y = 0;
            for(CreditsEntry entry : HeadCache.CREDITS)
            {
                gui = gui.withButton(new ChestGUIButton(entry.compile(), ()->{

                }, new Vector2i(x,y)));

                //LibZontreck.LOGGER.info("Add gui button : " + entry.name);

                y++;
                if(y>=9)
                {
                    x++;
                    y=0;
                }
            }

            gui.open();
            
            

            return 0;
        }else return 1;
    }

    
}
