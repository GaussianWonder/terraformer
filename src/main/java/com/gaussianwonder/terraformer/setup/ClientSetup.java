package com.gaussianwonder.terraformer.setup;

import com.gaussianwonder.terraformer.TerraformerMod;
import com.gaussianwonder.terraformer.setup.blocks.gui.MatterFuserScreen;
import com.gaussianwonder.terraformer.setup.blocks.gui.MatterRecyclerScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TerraformerMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void setup(final FMLClientSetupEvent event) {
        linkScreensToContainers();
    }

    private static void linkScreensToContainers() {
        ScreenManager.registerFactory(ModContainers.MATTER_RECYCLER_CONTAINER.get(), MatterRecyclerScreen::new);
        ScreenManager.registerFactory(ModContainers.MATTER_FUSER_CONTAINER.get(), MatterFuserScreen::new);
    }
}