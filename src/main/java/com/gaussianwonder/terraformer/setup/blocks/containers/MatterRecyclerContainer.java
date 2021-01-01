package com.gaussianwonder.terraformer.setup.blocks.containers;

import com.gaussianwonder.terraformer.setup.ModBlocks;
import com.gaussianwonder.terraformer.setup.ModContainers;
import com.gaussianwonder.terraformer.setup.blocks.tiles.MatterRecyclerTitle;
import com.gaussianwonder.terraformer.setup.capabilities.CapabilityMatter;
import com.gaussianwonder.terraformer.setup.capabilities.i_storage.IMatterStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

//TODO some sort of dictionary or tool that converts items into a quantity "MATTER" that somehow makes sense and is fair
public class MatterRecyclerContainer extends Container {
    private TileEntity tileEntity;
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;

    public MatterRecyclerContainer(int windowID, World world, BlockPos blockPos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        super(ModContainers.MATTER_RECYCLER_CONTAINER.get(), windowID);

        this.tileEntity = world.getTileEntity(blockPos);
        this.playerEntity = playerEntity;
        this.playerInventory = new InvWrapper(playerInventory);

        if(tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 28, 11));
            });
        }

        layoutPlayerInvetory(10, 70);
        initMatterRequest();
    }

    private void initMatterRequest() {
        //TODO track the damned matter when the GUI is opened instead of updating the tile at each matter change
        try { // Note that this is here ONLY because it waits for a MatterUpdate to send data update request
            ((MatterRecyclerTitle) this.tileEntity).requestMatterUpdate();
        }
        catch (Exception e) {
        }
    }

    public float getMatter() {
        return tileEntity.getCapability(CapabilityMatter.MATTER).map(IMatterStorage::getMatterStored).orElse(0.0f);
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for(int i=0; i<amount; ++i) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            ++index;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for(int j=0; j<verAmount; ++j) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInvetory(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, ModBlocks.MATTER_RECYCLER.get());
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemstack = stack.copy();
            if (index == 0) {
                if (!this.mergeItemStack(stack, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack, itemstack);
            } else {
                if (!stack.isEmpty()) {
                    if (!this.mergeItemStack(stack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 28) {
                    if (!this.mergeItemStack(stack, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 37 && !this.mergeItemStack(stack, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }

        return itemstack;
    }
}