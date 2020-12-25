package com.gaussianwonder.terraformer.setup.data;

import com.gaussianwonder.terraformer.TerraformerMod;
import com.gaussianwonder.terraformer.setup.data.client.ModBlockStateProvider;
import com.gaussianwonder.terraformer.setup.data.client.ModItemModelProvider;
import com.gaussianwonder.terraformer.setup.data.tags.ModBlockTagsProvider;
import com.gaussianwonder.terraformer.setup.data.tags.ModItemTagsProvider;
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

        gen.addProvider(new ModBlockStateProvider(gen, existingFileHelper));
        gen.addProvider(new ModItemModelProvider(gen, existingFileHelper));

        ModBlockTagsProvider modBlockTagsProvider = new ModBlockTagsProvider(gen, existingFileHelper);
        ModItemTagsProvider modItemTagsProvider = new ModItemTagsProvider(gen, modBlockTagsProvider, existingFileHelper);
        gen.addProvider(modBlockTagsProvider);
        gen.addProvider(modItemTagsProvider);
    }
}
