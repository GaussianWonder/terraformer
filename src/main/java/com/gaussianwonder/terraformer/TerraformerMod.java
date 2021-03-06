package com.gaussianwonder.terraformer;

import com.gaussianwonder.terraformer.setup.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TerraformerMod.MOD_ID)
public class TerraformerMod
{
    public static final String MOD_ID = "terraformer";
    public static final ItemGroup ITEM_GROUP = new ItemGroup("terraformerGroup") {
        /** Registering a new creative tab for TerraformerMod
         * This is here because TerraformerMod.GROUP makes A LOT OF SENSE,
         *  even though this is "registering" the terraformer group (implying it should be defined inside RegistryHandler)
         *  TerraformerMod.GROUP is arguably "better" than RegistryHandler.GROUP
         */
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.MATTER_FUSER_ITEM.get());
        }
    };

    private static final Logger LOGGER = LogManager.getLogger();

    public TerraformerMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);

        RegistryHandler.register();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::setup);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(RegistryHandler::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }
}
