package com.gaussianwonder.terraformer.setup.blocks.containers;

import com.gaussianwonder.terraformer.networking.PacketHandler;
import com.gaussianwonder.terraformer.networking.sync.MachineSyncMessage;
import com.gaussianwonder.terraformer.networking.sync.MatterSyncMessage;
import com.gaussianwonder.terraformer.setup.ModBlocks;
import com.gaussianwonder.terraformer.setup.ModContainers;
import com.gaussianwonder.terraformer.capabilities.CapabilityMachine;
import com.gaussianwonder.terraformer.capabilities.CapabilityMatter;
import com.gaussianwonder.terraformer.api.capabilities.handler.IMachineHandler;
import com.gaussianwonder.terraformer.api.capabilities.storage.IMatterStorage;
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
        syncData();
    }

    public int refreshRate() {
        return tileEntity.getCapability(CapabilityMachine.MACHINE).map(IMachineHandler::calculateCooldownTicks).orElse(20);
    }

    public void syncData() {
        PacketHandler.SYNC_CHANNEL.sendToServer(new MatterSyncMessage(tileEntity.getPos()));
        PacketHandler.SYNC_CHANNEL.sendToServer(new MachineSyncMessage(tileEntity.getPos()));
    }

    public float getMatter() {
        return tileEntity.getCapability(CapabilityMatter.MATTER).map(IMatterStorage::getMatterStored).orElse(new IMatterStorage.Matter()).getMatter();
    }

    public float getSpeedFactor() {
        return tileEntity.getCapability(CapabilityMachine.MACHINE).map(IMachineHandler::getSpeedProductionFactor).orElse(0.0f);
    }

    public float getOutputFactor() {
        return tileEntity.getCapability(CapabilityMachine.MACHINE).map(IMachineHandler::getOutputProductionFactor).orElse(0.0f);
    }

    public float getInputFactor() {
        return tileEntity.getCapability(CapabilityMachine.MACHINE).map(IMachineHandler::getInputSupplyFactor).orElse(0.0f);
    }

    public IMachineHandler.StatsRatio getStatsRatio() {
        return tileEntity.getCapability(CapabilityMachine.MACHINE).map(IMachineHandler::getRatio).orElse(new IMachineHandler.StatsRatio());
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
