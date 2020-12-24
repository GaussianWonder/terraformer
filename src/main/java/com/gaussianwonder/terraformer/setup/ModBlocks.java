package com.gaussianwonder.terraformer.setup;

import com.gaussianwonder.terraformer.TerraformerMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

//TODO Make this BaseBlock compatible
public class ModBlocks {
    //TODO Think about this for a moment, do you really want to access items like ITEM and blocks like BLOCK.BLOCK & BLOCK.ITEM ?
    public static final RegistryObject<Block> TEST_BLOCK = register("test_block", () ->
            new Block(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(3, 10).sound(SoundType.CHAIN)));

    static void register() {

    }

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block) {
        return RegistryHandler.BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        RegistryObject<T> ret = registerNoItem(name, block);
        RegistryHandler.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().group(TerraformerMod.ITEM_GROUP)));
        return ret;
    }
}
