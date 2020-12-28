package com.gaussianwonder.terraformer.setup.data;

import com.gaussianwonder.terraformer.TerraformerMod;
import com.gaussianwonder.terraformer.setup.data.client.ModBlockStateProvider;
import com.gaussianwonder.terraformer.setup.data.client.ModItemModelProvider;
import com.gaussianwonder.terraformer.setup.data.loot.LootTables;
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
        /**TODO Update the data generation system
         *TODO Since i want a class / item or block i can manage crafting recipes, loot tables, states and models
         *  through a ModBlock or ModItem extension
         *
         * I still need to consider this move, it will make each Item / Block class pretty big and the same goes for the
         *  DataGeneration.java file
         */
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        // Models and States
        gen.addProvider(new ModBlockStateProvider(gen, existingFileHelper));
        gen.addProvider(new ModItemModelProvider(gen, existingFileHelper));

        // Loot Tables
        gen.addProvider(new LootTables(gen));

        // Tags
        ModBlockTagsProvider modBlockTagsProvider = new ModBlockTagsProvider(gen, existingFileHelper);
        ModItemTagsProvider modItemTagsProvider = new ModItemTagsProvider(gen, modBlockTagsProvider, existingFileHelper);
        gen.addProvider(modBlockTagsProvider);
        gen.addProvider(modItemTagsProvider);
    }
}
