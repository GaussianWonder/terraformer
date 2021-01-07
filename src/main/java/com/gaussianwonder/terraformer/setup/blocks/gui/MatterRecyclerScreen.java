package com.gaussianwonder.terraformer.setup.blocks.gui;

import com.gaussianwonder.terraformer.TerraformerMod;
import com.gaussianwonder.terraformer.setup.blocks.containers.MatterRecyclerContainer;
import com.gaussianwonder.terraformer.capabilities.handler.IMachineHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.text.DecimalFormat;

//TODO implement shortcuts for ResourceLocation in a default abstract class of ContainerScreen<T> (this.mod() & this.forge())
public class MatterRecyclerScreen extends ContainerScreen<MatterRecyclerContainer> {
    private int currentTick;
    private int refreshRate;
    private final ResourceLocation GUI = mod("matter_recycler_gui.png");

    public MatterRecyclerScreen(MatterRecyclerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

        // Update trigger
        currentTick = 0;
        refreshRate = container.refreshRate();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        //TODO change this to an animated horizontal bar
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        drawString(matrixStack, Minecraft.getInstance().fontRenderer, "Matter Stored: " + df.format(container.getMatter()), 10, 40, 0xffffff);

        // Debug purpose only
        drawString(matrixStack, Minecraft.getInstance().fontRenderer, "Efficiency: " + df.format(container.getSpeedFactor()) + " - " + df.format(container.getOutputFactor()) + " - " + df.format(container.getInputFactor()), 10, 50, 0xffffff);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        assert this.minecraft != null;

        this.minecraft.getTextureManager().bindTexture(GUI);

        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);

        IMachineHandler.StatsRatio statsRatio = this.container.getStatsRatio();
        if (validStatsRatio(statsRatio)) {
            this.blit(matrixStack, relX + 150, relY + 11, 180, 0, 4, (int)(29 * statsRatio.speed));
            this.blit(matrixStack, relX + 158, relY + 11, 184, 0, 4, (int)(29 * statsRatio.output));
            this.blit(matrixStack, relX + 166, relY + 11, 188, 0, 4, (int)(29 * statsRatio.input));
        }
    }

    @Override
    public void tick() {
        System.out.println("Tick number: " + currentTick);
        if(currentTick >= refreshRate) {
            currentTick = 0;
            refreshRate = container.refreshRate();

            container.syncData();
        }

        ++currentTick;
        super.tick();
    }

    protected static ResourceLocation forge(String path) {
        return new ResourceLocation("forge", path);
    }

    protected static ResourceLocation mod(String name) {
        return new ResourceLocation(TerraformerMod.MOD_ID, "textures/gui/" + name);
    }

    private boolean validStatsRatio(IMachineHandler.StatsRatio statsRatio) {
        return statsRatio.speed != 0.0f && statsRatio.input != 0.0f && statsRatio.output != 0.0f;
    }
}
