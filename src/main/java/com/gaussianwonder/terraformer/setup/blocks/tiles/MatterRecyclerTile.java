package com.gaussianwonder.terraformer.setup.blocks.tiles;

import com.gaussianwonder.terraformer.setup.Config;
import com.gaussianwonder.terraformer.setup.ModItems;
import com.gaussianwonder.terraformer.setup.ModTiles;
import com.gaussianwonder.terraformer.capabilities.CapabilityMachine;
import com.gaussianwonder.terraformer.capabilities.CapabilityMatter;
import com.gaussianwonder.terraformer.capabilities.handler.IMachineHandler;
import com.gaussianwonder.terraformer.capabilities.handler.MachineHandler;
import com.gaussianwonder.terraformer.capabilities.storage.IMatterStorage;
import com.gaussianwonder.terraformer.capabilities.storage.MatterStorage;
import com.gaussianwonder.terraformer.setup.items.UpgradeItem;
import com.google.common.util.concurrent.AtomicDouble;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
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
            return; // only server-side processing
        }

        if(!machineHandler.isBusy()) {
            boolean failed = false;
            ItemStack toBeExtracted = itemHandler.getStackInSlot(0);
            if(!toBeExtracted.isEmpty()) {
                Item item = toBeExtracted.getItem();
                int maxExtractCount = (int) Math.min(toBeExtracted.getCount(), machineHandler.getInputSupplyFactor());

                if(item == ModItems.SPEED_UPGRADE.get())
                    machineHandler.change(IMachineHandler.Target.SPEED, maxExtractCount);
                else if(item == ModItems.SPEED_DOWNGRADE.get())
                    machineHandler.change(IMachineHandler.Target.SPEED, maxExtractCount * -1);
                else if(item == ModItems.OUTPUT_UPGRADE.get())
                    machineHandler.change(IMachineHandler.Target.OUTPUT, maxExtractCount);
                else if(item == ModItems.INPUT_UPGRADE.get())
                    machineHandler.change(IMachineHandler.Target.INPUT, maxExtractCount);
                else {
                    IMatterStorage.Matter matterProperties = Config.getDefaultsFor(item);
                    if(matterProperties instanceof Config.MatterConfig.Invalid)
                        failed = true;
                    matterStorage.receiveMatter(matterProperties.multiply(machineHandler.getOutputProductionFactor() * maxExtractCount),false);
                }
            }

            if(!failed) {
                itemHandler.extractItem(0, (int) Math.max(1, (int) Math.min(toBeExtracted.getCount(), machineHandler.getInputSupplyFactor())), false);
                machineHandler.busy();
                markDirty();
            }
        }

        machineHandler.tick();
        sendOutMatter();
    }

    private void sendOutMatter() {
        IMatterStorage.Matter capacity = matterStorage.getMatterStored();
        if(matterStorage.canExtract() && capacity.getMatter() > 0) {
            AtomicDouble solidCapacity = new AtomicDouble(capacity.solid);
            AtomicDouble softCapacity = new AtomicDouble(capacity.soft);
            AtomicDouble granularCapacity = new AtomicDouble(capacity.granular);

            Direction direction = Direction.DOWN;
            TileEntity tile = world.getTileEntity(pos.offset(direction));
            if(tile != null) {
                boolean canContinue = tile.getCapability(CapabilityMatter.MATTER, direction).map(handler -> {
                    if(handler.canReceive()) {
                        IMatterStorage.Matter received = handler.receiveMatter(
                                IMatterStorage.Matter.capByCapacity(
                                        new IMatterStorage.Matter(
                                                solidCapacity.floatValue(),
                                                softCapacity.floatValue(),
                                                granularCapacity.floatValue()
                                        ),
                                        matterStorage.getMaxExtract()
                                ),
                                false
                        );

                        if(received.getMatter() > 0.0f) {
                            solidCapacity.addAndGet(-received.solid);
                            softCapacity.addAndGet(-received.soft);
                            granularCapacity.addAndGet(-received.granular);

                            matterStorage.extractMatter(received, false);
                            markDirty();

                            return solidCapacity.get() + softCapacity.get() + granularCapacity.get() > 0.0f;
                        }
                        return true;
                    }
                    return true;
                }).orElse(true);

                if(!canContinue)
                    return;
            }
        }
    }

    public void checkNeighbors() {
        BlockPos top = new BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ());
        if(this.world != null && this.world.isBlockPresent(top)) {
            Block topBlock = this.world.getBlockState(top).getBlock();
            if(topBlock != Blocks.HOPPER)
                this.world.destroyBlock(top, true);
        }
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
                // Any Recyclable item can be thrown in here
                //TODO look at crafting recipes as well
                return Config.exists(stack.getItem()) || stack.getItem() instanceof UpgradeItem;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if(isItemValid(slot, stack))
                    return super.insertItem(slot, stack, simulate);
                else
                    return super.insertItem(slot, stack, true);
            }
        };
    }

    private MatterStorage createMatter() {
        return new MatterStorage(1000.0f, 25.0f) {
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
