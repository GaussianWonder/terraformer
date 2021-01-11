package com.gaussianwonder.terraformer.setup;

import com.gaussianwonder.terraformer.setup.blocks.tiles.MatterFuserTile;
import com.gaussianwonder.terraformer.setup.blocks.tiles.MatterRecyclerTile;
import com.gaussianwonder.terraformer.setup.blocks.tiles.MatterStorageTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

public class ModTiles {
    public static final RegistryObject<TileEntityType<MatterRecyclerTile>> MATTER_RECYCLER_TILE = RegistryHandler.TILES.register(
            "matter_recycler",
            () -> TileEntityType.Builder
                    .create(MatterRecyclerTile::new, ModBlocks.MATTER_RECYCLER.get())
                    .build(null)
        );
    public static final RegistryObject<TileEntityType<MatterFuserTile>> MATTER_FUSER_TILE = RegistryHandler.TILES.register(
            "matter_fuser",
            () -> TileEntityType.Builder
                    .create(MatterFuserTile::new, ModBlocks.MATTER_FUSER.get())
                    .build(null)
    );
    public static final RegistryObject<TileEntityType<MatterStorageTile>> MATTER_STORAGE_TILE = RegistryHandler.TILES.register(
            "matter_storage",
            () -> TileEntityType.Builder
                    .create(MatterStorageTile::new, ModBlocks.MATTER_STORAGE.get())
                    .build(null)
    );

    static void register() { }
}
