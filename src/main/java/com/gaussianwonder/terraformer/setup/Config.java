package com.gaussianwonder.terraformer.setup;

import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ObjectHolder;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber
public class Config {
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_DICTIONARY = "dictionary";

    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    static {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        CLIENT_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        // Client config
        CLIENT_BUILDER.pop();

        SERVER_BUILDER.comment("Matter Dictionary").push(CATEGORY_DICTIONARY);
        // Server config
        setupMatterDictionary(SERVER_BUILDER, CLIENT_BUILDER);
        SERVER_BUILDER.pop();


        SERVER_CONFIG = SERVER_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupMatterDictionary(ForgeConfigSpec.Builder SERVER_BUILDER, ForgeConfigSpec.Builder CLIENT_BUILDER) {
    }


    private static void addDictionaryFor(ForgeConfigSpec.Builder BUILDER, Item item) {
//        ItemStack sads = new ItemStack(Blocks.IRON_BLOCK, 126);
//        sads.setCount();
//        BUILDER.comment(item.)
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {
    }
}
