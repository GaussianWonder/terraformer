package com.gaussianwonder.terraformer.setup.blocks.tiles;

import com.gaussianwonder.terraformer.setup.ModTiles;
import com.gaussianwonder.terraformer.setup.capabilities.CapabilityMachine;
import com.gaussianwonder.terraformer.setup.capabilities.CapabilityMatter;
import com.gaussianwonder.terraformer.setup.capabilities.handler.IMachineHandler;
import com.gaussianwonder.terraformer.setup.capabilities.handler.MachineHandler;
import com.gaussianwonder.terraformer.setup.capabilities.storage.IMatterStorage;
import com.gaussianwonder.terraformer.setup.capabilities.storage.MatterStorage;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class MatterRecyclerTile extends TileEntity implements ITickableTileEntity {
    private ItemStackHandler itemHandler = createHandler();
    private MatterStorage matterStorage = createMatter();
    private MachineHandler machineHandler = createMachine();

    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private LazyOptional<IMatterStorage> matter = LazyOptional.of(() -> matterStorage);
    private LazyOptional<IMachineHandler> machine = LazyOptional.of(() -> machineHandler);

    public MatterRecyclerTile() {
        super(ModTiles.MATTER_RECYCLER_TILE.get());
    }

    @Override
    public void tick() {
        if(world.isRemote) {
            String details = machineHandler.getSpeedProductionFactor() + " " + machineHandler.getOutputProductionFactor() + " " + machineHandler.getInputSupplyFactor();
            System.out.println("From client " + "(" + details + "):" + matterStorage.getMatterStored());
            return; // only server-side processing
        } else {
            String details = machineHandler.getSpeedProductionFactor() + " " + machineHandler.getOutputProductionFactor() + " " + machineHandler.getInputSupplyFactor();
            System.out.println("From server " + "(" + details + "):" + matterStorage.getMatterStored());
        }

        if(!machineHandler.isBusy()) {
            ItemStack stack = itemHandler.getStackInSlot(0);

            if(stack.getItem() == Items.DIAMOND) { //TODO change this to proper upgrade item
                machineHandler.change(IMachineHandler.Target.SPEED, 1);
            }
            else if(!stack.isEmpty()){
                //TODO Convert the Item into Matter according to a Dictionary
                matterStorage.receiveMatter(machineHandler.getOutputProductionFactor(), false);
            }

            itemHandler.extractItem(0, (int) Math.max(1, machineHandler.getInputSupplyFactor()), false);

            machineHandler.busy();
            markDirty();
        }

        machineHandler.tick();
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        itemHandler.deserializeNBT(tag.getCompound("inv"));
        matterStorage.deserializeNBT(tag.getCompound("matter"));
        machineHandler.deserializeNBT(tag.getCompound("machine"));
        super.read(state, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("inv", itemHandler.serializeNBT());
        tag.put("matter", matterStorage.serializeNBT());
        tag.put("machine", machineHandler.serializeNBT());
        return super.write(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, /* @Nullable */ Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
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
        handler.invalidate();
        matter.invalidate();
        machine.invalidate();
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true; // Anything can be inserted into the recycler
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return stack; // Anything can be inserted into the recycler
                // return super.insertItem(slot, stack, simulate);
            }
        };
    }

    private MatterStorage createMatter() {
        return new MatterStorage(1000000.0f, 100.0f) {
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