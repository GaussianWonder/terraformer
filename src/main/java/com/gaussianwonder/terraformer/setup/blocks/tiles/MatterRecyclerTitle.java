package com.gaussianwonder.terraformer.setup.blocks.tiles;

import com.gaussianwonder.terraformer.setup.ModTiles;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class MatterRecyclerTitle extends TileEntity implements ITickableTileEntity {
    public MatterRecyclerTitle() {
        super(ModTiles.MATTER_RECYCLER_TILE.get());
    }

    @Override
    public void tick() {

    }
}
