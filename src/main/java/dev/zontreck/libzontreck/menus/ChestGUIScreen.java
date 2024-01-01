package dev.zontreck.libzontreck.menus;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.chestgui.ChestGUI;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class ChestGUIScreen extends AbstractContainerScreen<ChestGUIMenu> {
    public final Player player;
    public final ChestGUIMenu menu;
    private static final ResourceLocation TEXTURE = new ResourceLocation(LibZontreck.MOD_ID, "textures/gui/chest_gui.png");


    public ChestGUIScreen(ChestGUIMenu menu, Inventory playerInv, Component comp)
    {
        super(menu, playerInv, comp);

        this.menu = menu;
        this.player = playerInv.player;

        this.leftPos = 0;
        this.topPos=0;

        this.imageWidth = 191;
        this.imageHeight = 82;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void init() {
        super.init();

        this.inventoryLabelX = 32;
        this.inventoryLabelY = 5;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        renderBackground(guiGraphics);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0, TEXTURE);

        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(font, title.getString(), 32, 5, 0x000000);
    }
}
