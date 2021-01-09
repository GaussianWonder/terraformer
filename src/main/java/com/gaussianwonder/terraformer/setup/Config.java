package com.gaussianwonder.terraformer.setup;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.gaussianwonder.terraformer.capabilities.storage.IMatterStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

@Mod.EventBusSubscriber
public class Config {
    private static String CONFIG_PATH;

    public static class MatterConfig {
        public static class Defaults extends IMatterStorage.Matter {
            Defaults(float solid, float soft, float granular) { super(solid, soft, granular); }
        }
        public static class Invalid extends Defaults { Invalid(float solid, float soft, float granular) { super(solid, soft, granular); } }

        ForgeConfigSpec.DoubleValue SOLID;
        ForgeConfigSpec.DoubleValue SOFT;
        ForgeConfigSpec.DoubleValue GRANULAR;

        MatterConfig(ForgeConfigSpec.DoubleValue solid, ForgeConfigSpec.DoubleValue soft, ForgeConfigSpec.DoubleValue granular) {
            this.SOLID = solid;
            this.SOFT = soft;
            this.GRANULAR = granular;
        }
    }
    public static HashMap<String, MatterConfig> matterDictionary = new HashMap<>();

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_DICTIONARY = "dictionary";
    public static final String CATEGORY_CUSTOM_DICTIONARY = "custom_dictionary";
    public static final String CATEGORY_MOD_GENERATED = "mod_generated";

    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    static {
        CONFIG_PATH = Minecraft.getInstance().gameDir.getAbsolutePath(); //TODO research, no priority here

        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        CLIENT_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        // Client config
        CLIENT_BUILDER.pop();

        CLIENT_BUILDER.comment("Custom Dictionary").push(CATEGORY_CUSTOM_DICTIONARY);
        // Don't touch this, it is only for the player to customize
        CLIENT_BUILDER.pop();

        SERVER_BUILDER.comment("Matter Dictionary").push(CATEGORY_DICTIONARY);
        // Server config
        setupMatterDictionary(SERVER_BUILDER);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.comment("Mod Generated Content").push(CATEGORY_MOD_GENERATED);
        setupCustomMatterDictionary(SERVER_BUILDER);
        SERVER_BUILDER.pop();

        SERVER_CONFIG = SERVER_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void addDictionaryFor(ForgeConfigSpec.Builder BUILDER, Item item, MatterConfig.Defaults defaults) {
        ResourceLocation rl = ForgeRegistries.ITEMS.getKey(item);
        if(rl == null || defaults == null) return;
        // can't make config options for items without resource locations (if that's even a thing)
        // can't make a config option for items without default values

        String itemLocation = rl.toString();
        BUILDER.comment("Settings for " + itemLocation).push(itemLocation);

        ForgeConfigSpec.DoubleValue SOLID = BUILDER.comment("Solid Matter returned")
                .defineInRange("solid_matter", defaults.solid, 0, Float.MAX_VALUE);

        ForgeConfigSpec.DoubleValue SOFT = BUILDER.comment("Soft Matter returned")
                .defineInRange("soft_matter", defaults.soft, 0, Float.MAX_VALUE);

        ForgeConfigSpec.DoubleValue GRANULAR = BUILDER.comment("Granular Matter returned")
                .defineInRange("granular_matter", defaults.granular, 0, Float.MAX_VALUE);

        matterDictionary.put(itemLocation, new MatterConfig(
                SOLID,
                SOFT,
                GRANULAR
        ));

        BUILDER.pop();
    }

    public static MatterConfig.Defaults getDefaultsFor(Item item) {
        ResourceLocation rl = ForgeRegistries.ITEMS.getKey(item);
        if(rl == null) return new MatterConfig.Invalid(0.0f,0.0f,0.0f);
        MatterConfig defaults = matterDictionary.get(rl.toString());

        return new MatterConfig.Defaults(
                defaults.SOLID.get().floatValue(),
                defaults.SOFT.get().floatValue(),
                defaults.GRANULAR.get().floatValue()
        );
    }

    //TODO make a version to check CRAFTING RECIPES as well, maybe an IRecyclable + Recyclable + CapabilityRecyclable
    public static boolean exists(Item item) {
        ResourceLocation rl = ForgeRegistries.ITEMS.getKey(item);
        if(rl == null) return false;
        return matterDictionary.get(rl.toString()) != null;
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {
    }

    private static void setupCustomMatterDictionary(ForgeConfigSpec.Builder BUILDER) {
        try {
            FileConfig config = FileConfig.of(new File(CONFIG_PATH + "/CustomTerraformerDictionary.toml"));
            config.load();
            ArrayList<String> itemNames = config.get("item_resources");
            for (String itemName : itemNames) {
                String tomlKey = itemName.replaceAll(":", "_").replaceAll("/", "_");
                RegistryObject<Item> itemRegistry = RegistryObject.of(new ResourceLocation(itemName), ForgeRegistries.ITEMS);

                if(itemRegistry.isPresent()) {
                    Item item = itemRegistry.get();
                    ArrayList<Float> defaultConfig = config.get(tomlKey);
                    if (defaultConfig.size() == 3) {
                        Float defaultSolid = defaultConfig.get(0);
                        Float defaultSoft = defaultConfig.get(1);
                        Float defaultGranular = defaultConfig.get(2);
                        System.out.println("Registered " + itemName);
                        addDictionaryFor(BUILDER, item, new MatterConfig.Defaults(
                                defaultSolid,
                                defaultSoft,
                                defaultGranular
                        ));
                    }
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong, full abort");
        }

//        BUILDER.comment("Something went wrong when reading or parsing the other file")
//                .comment("Be sure you have privilages to create and edit files"); //TODO make this appear in the toml file if anything went wrong
//        addDictionaryFor(BUILDER, Items.DEAD_BUSH, new MatterConfig.Defaults(
//                0.0f,
//                0.0f,
//                0.00001f
//        ));
    }

    private static void setupMatterDictionary(ForgeConfigSpec.Builder SERVER_BUILDER) {
        // SOLID
        addDictionaryFor(SERVER_BUILDER, Items.STONE, new MatterConfig.Defaults(
                1.0f,
                0.0f,
                0.0f
        ));
        addDictionaryFor(SERVER_BUILDER, Items.GRANITE, new MatterConfig.Defaults(
                1.5f,
                0.0f,
                0.0f
        ));
        addDictionaryFor(SERVER_BUILDER, Items.DIORITE, new MatterConfig.Defaults(
                1.5f,
                0.0f,
                0.0f
        ));
        addDictionaryFor(SERVER_BUILDER, Items.ANDESITE, new MatterConfig.Defaults(
                1.5f,
                0.0f,
                0.0f
        ));
        addDictionaryFor(SERVER_BUILDER, Items.COBBLESTONE, new MatterConfig.Defaults(
                0.8f,
                0.0f,
                0.1f
        ));

        // SOFT
        addDictionaryFor(SERVER_BUILDER, Items.CLAY, new MatterConfig.Defaults(
                0.0f,
                1.0f,
                0.0f
        ));
        addDictionaryFor(SERVER_BUILDER, Items.GRAVEL, new MatterConfig.Defaults(
                0.1f,
                0.1f,
                0.8f
        ));

        // GRANULAR
        addDictionaryFor(SERVER_BUILDER, Items.SAND, new MatterConfig.Defaults(
                0.0f,
                0.0f,
                1.0f
        ));
        addDictionaryFor(SERVER_BUILDER, Items.RED_SAND, new MatterConfig.Defaults(
                0.0f,
                0.1f,
                0.8f
        ));
    }
}
