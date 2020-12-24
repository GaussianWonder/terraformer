package com.gaussianwonder.terraformer.data;

import com.gaussianwonder.terraformer.TerraformerMod;
import com.gaussianwonder.terraformer.data.client.TerraformerBlockStateProvider;
import com.gaussianwonder.terraformer.data.client.TerraformerItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = TerraformerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    public DataGenerators() { }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        gen.addProvider(new TerraformerBlockStateProvider(gen, existingFileHelper));
        gen.addProvider(new TerraformerItemModelProvider(gen, existingFileHelper));
    }
}
