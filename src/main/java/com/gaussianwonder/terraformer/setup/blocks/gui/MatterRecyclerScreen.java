package com.gaussianwonder.terraformer.setup.blocks.gui;

import com.gaussianwonder.terraformer.TerraformerMod;
import com.gaussianwonder.terraformer.setup.blocks.containers.MatterRecyclerContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

//TODO implement shortcuts for ResourceLocation in a default abstract class of ContainerScreen<T> (this.mod() & this.forge())
public class MatterRecyclerScreen extends ContainerScreen<MatterRecyclerContainer> {
    private final ResourceLocation GUI = mod("matter_recycler_gui.png");

    public MatterRecyclerScreen(MatterRecyclerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        drawString(matrixStack, Minecraft.getInstance().fontRenderer, "Matter Stored: " + container.getMatter(), 10, 40, 0xffffff);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        assert this.minecraft != null;

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.minecraft.getTextureManager().bindTexture(GUI);

        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
    }

    protected static ResourceLocation forge(String path) {
        return new ResourceLocation("forge", path);
    }

    protected static ResourceLocation mod(String name) {
        return new ResourceLocation(TerraformerMod.MOD_ID, "textures/gui/" + name);
    }
}
