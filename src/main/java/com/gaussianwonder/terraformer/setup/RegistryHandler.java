package com.gaussianwonder.terraformer.setup;

import com.gaussianwonder.terraformer.TerraformerMod;
import com.gaussianwonder.terraformer.networking.PacketHandler;
import com.gaussianwonder.terraformer.capabilities.CapabilityMachine;
import com.gaussianwonder.terraformer.capabilities.CapabilityMatter;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TerraformerMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TerraformerMod.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, TerraformerMod.MOD_ID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, TerraformerMod.MOD_ID);

    public static void register() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        TILES.register(eventBus);
        CONTAINERS.register(eventBus);

        ModItems.register();
        ModBlocks.register();
        ModTiles.register();
        ModContainers.register();

        MatterDictionary.register();
    }

    public static void commonSetup(final FMLClientSetupEvent event) {
        PacketHandler.registerMessages();

        CapabilityMatter.register();
        CapabilityMachine.register();
    }
}
