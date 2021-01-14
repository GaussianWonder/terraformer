package com.gaussianwonder.terraformer.setup.blocks.gui;

import com.gaussianwonder.terraformer.TerraformerMod;
import com.gaussianwonder.terraformer.api.capabilities.handler.IMachineHandler;
import com.gaussianwonder.terraformer.setup.blocks.containers.MatterFuserContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.text.DecimalFormat;

public class MatterFuserScreen extends ContainerScreen<MatterFuserContainer> {
    private int currentTick;
    private int refreshRate;
    private final ResourceLocation GUI = mod("matter_fuser_gui.png");

    public MatterFuserScreen(MatterFuserContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
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
        //TODO clean hasItemInside and getCompleteProgress
        this.blit(matrixStack, relX + 51, relY + 16, 192, 0, (int)(16 * ((float) currentTick / refreshRate)) * (container.hasItemInside() ? 1 : 0), 5);
    }

    @Override
    public void tick() {
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
