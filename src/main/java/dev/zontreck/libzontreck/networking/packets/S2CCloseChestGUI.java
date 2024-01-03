package dev.zontreck.libzontreck.networking.packets;

import dev.zontreck.libzontreck.chestgui.ChestGUI;
import dev.zontreck.libzontreck.menus.ChestGUIScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CCloseChestGUI
{

    public S2CCloseChestGUI()
    {

    }

    public S2CCloseChestGUI(FriendlyByteBuf buf)
    {

    }

    public void toBytes(FriendlyByteBuf buf)
    {

    }

    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context ctx = supplier.get();

        ctx.enqueueWork(()->{
            // Close the GUI
            if(Minecraft.getInstance().screen instanceof ChestGUIScreen)
                Minecraft.getInstance().setScreen(null);
        });
    }
}
