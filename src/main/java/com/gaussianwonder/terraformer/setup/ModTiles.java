package com.gaussianwonder.terraformer.setup;

import com.gaussianwonder.terraformer.setup.blocks.tiles.MatterRecyclerTitle;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

public class ModTiles {
    public static final RegistryObject<TileEntityType<MatterRecyclerTitle>> MATTER_RECYCLER_TILE = RegistryHandler.TILES.register(
            "matter_recycler",
            () -> TileEntityType.Builder
                    .create(MatterRecyclerTitle::new, ModBlocks.MATTER_RECYCLER.get())
                    .build(null)
        );

    static void register() { }
}
