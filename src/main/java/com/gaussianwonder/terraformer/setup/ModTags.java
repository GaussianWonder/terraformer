package com.gaussianwonder.terraformer.setup;

import com.gaussianwonder.terraformer.TerraformerMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ModTags {
    public static final class Blocks {
        // Block block tags
        public static final ITag.INamedTag<Block> TESTS_BLOCK = mod("tests/block");
        public static final ITag.INamedTag<Block> MATTER_UTILS = mod("matter/utils");

        // Tag Shortcuts
        private static ITag.INamedTag<Block> forge(String path) {
            return BlockTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Block> mod(String path) {
            return BlockTags.makeWrapperTag(new ResourceLocation(TerraformerMod.MOD_ID, path).toString());
        }
    }

    public static final class Items {
        // Item tags
        public static final ITag.INamedTag<Item> RODS_TERRAFORM = mod("rods/terraform");
        public static final ITag.INamedTag<Item> UPGRADES_MACHINE = mod("upgrades/machine");

        // Block Item tags
        public static final ITag.INamedTag<Item> TESTS_ITEM = mod("tests/item");
        public static final ITag.INamedTag<Item> MATTER_UTILS = mod("matter/utils");

        // Tag Shortcuts
        private static ITag.INamedTag<Item> forge(String path) {
            return ItemTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Item> mod(String path) {
            return ItemTags.makeWrapperTag(new ResourceLocation(TerraformerMod.MOD_ID, path).toString());
        }
    }
}
