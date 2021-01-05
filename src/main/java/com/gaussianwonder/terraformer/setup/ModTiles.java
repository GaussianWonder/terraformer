package com.gaussianwonder.terraformer.setup;

import com.gaussianwonder.terraformer.setup.blocks.tiles.MatterRecyclerTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

public class ModTiles {
    public static final RegistryObject<TileEntityType<MatterRecyclerTile>> MATTER_RECYCLER_TILE = RegistryHandler.TILES.register(
            "matter_recycler",
            () -> TileEntityType.Builder
                    .create(MatterRecyclerTile::new, ModBlocks.MATTER_RECYCLER.get())
                    .build(null)
        );

    static void register() { }
}
