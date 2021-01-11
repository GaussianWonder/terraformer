package com.gaussianwonder.terraformer.setup.blocks.tiles;

import com.gaussianwonder.terraformer.capabilities.CapabilityMachine;
import com.gaussianwonder.terraformer.capabilities.CapabilityMatter;
import com.gaussianwonder.terraformer.capabilities.handler.IMachineHandler;
import com.gaussianwonder.terraformer.capabilities.handler.MachineHandler;
import com.gaussianwonder.terraformer.capabilities.storage.IMatterStorage;
import com.gaussianwonder.terraformer.capabilities.storage.MatterStorage;
import com.gaussianwonder.terraformer.setup.ModTiles;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class MatterStorageTile extends TileEntity implements ITickableTileEntity {
    private MatterStorage matterStorage = createMatter();
    private MachineHandler machineHandler = createMachine();

    private LazyOptional<IMatterStorage> matter = LazyOptional.of(() -> matterStorage);
    private LazyOptional<IMachineHandler> machine = LazyOptional.of(() -> machineHandler);

    public MatterStorageTile() {
        super(ModTiles.MATTER_STORAGE_TILE.get());
    }

    @Override
    public void tick() {
        if(world.isRemote) {
//            String details = machineHandler.getSpeedProductionFactor() + " " + machineHandler.getOutputProductionFactor() + " " + machineHandler.getInputSupplyFactor();
//            System.out.println("From client " + "(" + details + "):" + matterStorage.getMatterStored());
            return; // only server-side processing
        } else {
            System.out.println("From server: " + matterStorage.getMatterStored().getMatter());
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        matterStorage.deserializeNBT(tag.getCompound("matter"));
        machineHandler.deserializeNBT(tag.getCompound("machine"));
        super.read(state, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("matter", matterStorage.serializeNBT());
        tag.put("machine", machineHandler.serializeNBT());
        return super.write(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, /* @Nullable */ Direction side) {
        if(cap == CapabilityMatter.MATTER) {
            return matter.cast();
        }
        if(cap == CapabilityMachine.MACHINE) {
            return machine.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        super.remove();
        matter.invalidate();
        machine.invalidate();
    }

    private MatterStorage createMatter() {
        return new MatterStorage(10000.0f, 65.0f) {
            @Override
            public void onMatterChange() {
                markDirty();
            }
        };
    }

    private MachineHandler createMachine() {
        return new MachineHandler(
                new IMachineHandler.Stats( // base
                        20,
                        1,
                        1
                ),
                new IMachineHandler.Stats( // init upgrades
                        0,
                        0,
                        0
                )
        ) {
            @Override
            public void onStatsChange() {
                markDirty();
            }
        };
    }

    public void updateClient(MatterStorage updatedMatterStorage) {
        matterStorage.update(updatedMatterStorage);
        markDirty();
    }

    public void updateClient(MachineHandler updatedMachineHandler) {
        machineHandler.setBaseStats(updatedMachineHandler.getBaseStats());
        machineHandler.setUpgradedStats(updatedMachineHandler.getUpgradedStats());
        markDirty();
    }

    public MatterStorage getCurrentMatter() {
        return new MatterStorage(
                matterStorage.getMatterStored(),
                matterStorage.getMaxMatterStored(),
                matterStorage.getMaxReceived(),
                matterStorage.getMaxExtract()
        );
    }

    public MachineHandler getCurrentMachine() {
        return new MachineHandler(
                machineHandler.getBaseStats(),
                machineHandler.getUpgradedStats()
        );
    }
}
