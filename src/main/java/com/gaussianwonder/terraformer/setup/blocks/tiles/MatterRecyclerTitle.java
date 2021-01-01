package com.gaussianwonder.terraformer.setup.blocks.tiles;

import com.gaussianwonder.terraformer.networking.MatterUpdateMessage;
import com.gaussianwonder.terraformer.networking.PacketHandler;
import com.gaussianwonder.terraformer.setup.ModTiles;
import com.gaussianwonder.terraformer.setup.capabilities.CapabilityMatter;
import com.gaussianwonder.terraformer.setup.capabilities.i_storage.IMatterStorage;
import com.gaussianwonder.terraformer.setup.capabilities.storage.MatterStorage;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class MatterRecyclerTitle extends TileEntity implements ITickableTileEntity {
    private ItemStackHandler itemHandler = createHandler();
    private MatterStorage matterStorage = createMatter();

    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private LazyOptional<IMatterStorage> matter = LazyOptional.of(() -> matterStorage);

    private final int baseCooldown = 20; // 20 tick() calls
    private int cooldown = calculateCooldownTicks();
    private int efficiency = 0;

    public MatterRecyclerTitle() {
        super(ModTiles.MATTER_RECYCLER_TILE.get());
    }

    @Override
    public void tick() {
        if(world.isRemote) {
            System.out.println("From client " + "(" + efficiency + "):" + matterStorage.getMatterStored());
            return; // only server-side processing
        } else {
            System.out.println("From server " + "(" + efficiency + "):" + matterStorage.getMatterStored());
        }

        if(cooldown <= 0) { // cooldown is over, you can recycle one more item
            ItemStack stack = itemHandler.getStackInSlot(0);

            if(stack.getItem() == Items.DIAMOND) { //TODO change this to proper upgrade item
                ++efficiency;
            }
            else if(!stack.isEmpty()){
                //TODO Convert the Item into Matter according to a Dictionary
                matterStorage.receiveMatter(1, false);
            }

            itemHandler.extractItem(0, 1, false);
            cooldown = calculateCooldownTicks();

            markDirty();
        }
        else {
            --cooldown;
            markDirty();
        }
    }

    public void clientUpdateMatter(float matter, float capacity, float maxReceive, float maxExtract) {
        matterStorage.setMatter(matter);
        matterStorage.setMaxMatterStored(capacity);
        matterStorage.setMaxReceived(maxReceive);
        matterStorage.setMaxExtract(maxExtract);

        markDirty();
    }

    /**
     * Formula: { 0.4 + [ 1 / (e ^ upgrades/10) ] } * baseCooldown.
     * Meaning:
     *  At first it is worse than expected, working at 1.4x the speed.
     *  Best it can do is 0.4x the base working tick speed after a handful of upgrades.
     *  Upgrading infinitely does nothing.
     * @return the number of ticks before recycling the next item
     */
    private int calculateCooldownTicks() {
        double lowerBase = 0.4;
        double graphSlope = 1 / Math.pow(Math.E, (double)efficiency / 10);
        return Math.max((int)((lowerBase + graphSlope ) * baseCooldown), 2); // safeguards
    }

    @Override
    public void remove() {
        super.remove();
        handler.invalidate();
        matter.invalidate();
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        itemHandler.deserializeNBT(tag.getCompound("inv"));
        matterStorage.deserializeNBT(tag.getCompound("matter"));

        cooldown = tag.getInt("cooldown");
        efficiency = tag.getInt("efficiency");
        super.read(state, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("inv", itemHandler.serializeNBT());
        tag.put("matter", matterStorage.serializeNBT());

        tag.putInt("cooldown", cooldown);
        tag.putInt("efficiency", efficiency);
        return super.write(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, /*@Nullable*/ Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        if(cap == CapabilityMatter.MATTER) {
            return matter.cast();
        }
        return super.getCapability(cap, side);
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

    public void requestMatterUpdate() {
        //TODO change this later into a complex networking solution that actually updates only when viewing the GUI
        if (getWorld() != null && !getWorld().isRemote) {
            BlockPos blockPos = getPos();
            PacketHandler.MATTER_CHANNEL.send(
                    PacketDistributor.NEAR.with( // Send to everyone NEAR
                            () -> new PacketDistributor.TargetPoint(
                                    blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                                    500,
                                    getWorld().getDimensionKey()
                            )
                    ),
                    new MatterUpdateMessage(matterStorage, blockPos)
            );
        }
    }

    private MatterStorage createMatter() {
        return new MatterStorage(10000.0f, 0.5f) {
            @Override
            protected void onMatterChange() {
                requestMatterUpdate();
                markDirty();
            }
        };
    }
}
