package com.gaussianwonder.terraformer.setup.blocks;

import com.gaussianwonder.terraformer.setup.RegistryHandler;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlock<BlockType extends BaseBlock, BlockItemType extends BaseBlockItem> {
    public RegistryObject<BlockType> block;
    public RegistryObject<BlockItemType> item; // I don't think i'll ever need BlockItemType

    public ModBlock(String name, final Supplier<? extends BlockType> blockSupplier) {
        final Function<BlockType, BaseBlockItem> blockItemSupplier = (BlockType block) -> new BaseBlockItem(block);

        block = RegistryHandler.BLOCKS.register(name, blockSupplier);
        item = RegistryHandler.ITEMS.register(name, () -> (BlockItemType) blockItemSupplier.apply(get()));
    }

    public ModBlock(String name, final Supplier<? extends BlockType> blockSupplier, Item.Properties blockItemProperties) {
        final Function<BlockType, BaseBlockItem> blockItemSupplier = (BlockType block) -> new BaseBlockItem(block, blockItemProperties);

        block = RegistryHandler.BLOCKS.register(name, blockSupplier);
        item = RegistryHandler.ITEMS.register(name, () -> (BlockItemType) blockItemSupplier.apply(get()));
    }

    public ModBlock(String name, final Supplier<? extends BlockType> blockSupplier, final ItemSupplier<BlockItemType, BlockType> blockItemSupplier) {
        block = RegistryHandler.BLOCKS.register(name, blockSupplier);
        item = RegistryHandler.ITEMS.register(name, () -> blockItemSupplier.get(block.get()));
    }

    public BlockType get() {
        return block.get();
    }

    public BlockItemType getItem() {
        return item.get();
    }

    @FunctionalInterface
    public interface ItemSupplier<BlockItemType, BlockType> {
        BlockItemType get(BlockType blockType);
    }
}