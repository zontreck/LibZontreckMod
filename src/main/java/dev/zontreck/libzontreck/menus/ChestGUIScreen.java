package dev.zontreck.libzontreck.menus;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.chestgui.ChestGUI;
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

        this.titleLabelX = 32;
        this.titleLabelY =5;
    }

    @Override
    public void render(PoseStack pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
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
    protected void renderBg(PoseStack guiGraphics, float v, int i, int i1) {
        renderBackground(guiGraphics);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0, TEXTURE);

        blit(guiGraphics, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(PoseStack pGuiGraphics, int pMouseX, int pMouseY) {

        drawString(pGuiGraphics, this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752);
    }
}
