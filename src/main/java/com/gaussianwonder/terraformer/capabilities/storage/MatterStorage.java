package com.gaussianwonder.terraformer.capabilities.storage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class MatterStorage implements IMatterStorage, INBTSerializable<CompoundNBT> {
    protected Matter matter;
    protected float capacity;
    protected float maxReceive;
    protected float maxExtract;

    public MatterStorage(float capacity)
    {
        this(new Matter(), capacity, capacity, capacity);
    }

    public MatterStorage(float capacity, float maxTransfer)
    {
        this(new Matter(), capacity, maxTransfer, maxTransfer);
    }

    public MatterStorage(float capacity, float maxReceive, float maxExtract)
    {
        this(new Matter(), capacity, maxReceive, maxExtract);
    }

    public MatterStorage(Matter matter, float capacity, float maxReceive, float maxExtract)
    {
        this.matter = new Matter(matter, capacity); // cap the matter to the capacity
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    public void onMatterChange() { }

    public Matter receiveMatter(Matter maxReceive, boolean simulate) {
        if (!canReceive())
            return new Matter();

        Matter maxMatterToReceive = Matter.capByCapacity(maxReceive, this.maxReceive);
        Matter actualReceivedMatter = Matter.capByCapacity(maxMatterToReceive, capacity - matter.getMatter());
        if (!simulate) {
            matter.add(actualReceivedMatter);
            onMatterChange();
        }

        return actualReceivedMatter;
    }

    public Matter extractMatter(Matter maxExtract, boolean simulate) {
        if (!canExtract())
            return new Matter();

        Matter maxMatterToExtract = Matter.capByCapacity(maxExtract, this.maxExtract);
        Matter actualReceivedMatter = Matter.capByCapacity(maxMatterToExtract, matter.getMatter());
        if (!simulate) {
            matter.substract(actualReceivedMatter);
            onMatterChange();
        }

        return actualReceivedMatter;
    }

    public boolean canExtract() {
        return this.maxExtract > 0.0f;
    }

    public boolean canReceive() {
        return this.maxReceive > 0.0f;
    }

    public Matter getMatterStored() {
        return new Matter(matter);
    }

    public float getMaxMatterStored() {
        return capacity;
    }

    public float getMaxReceived() {
        return maxReceive;
    }

    public float getMaxExtract() {
        return maxExtract;
    }

    public void setMaxReceived(float maxReceive) {
        this.maxReceive = maxReceive;
        onMatterChange();
    }

    public void setMaxExtract(float maxExtract) {
        this.maxExtract = maxExtract;
        onMatterChange();
    }

    public void setMatter(Matter matter) {
        this.matter = matter;
        onMatterChange();
    }

    public void setMaxMatterStored(float capacity) {
        this.capacity = capacity;
        onMatterChange();
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();

        tag.putFloat("solid_matter_stored", matter.solid);
        tag.putFloat("soft_matter_stored", matter.soft);
        tag.putFloat("granular_matter_stored", matter.granular);

        tag.putFloat("matter_capacity", getMaxMatterStored());
        tag.putFloat("matter_receive", getMaxReceived());
        tag.putFloat("matter_extract", getMaxExtract());

        return tag;
    }

    public void deserializeNBT(CompoundNBT nbt) {
        setMatter(new Matter(
                nbt.getFloat("solid_matter_stored"),
                nbt.getFloat("soft_matter_stored"),
                nbt.getFloat("granular_matter_stored")
        ));

        setMaxMatterStored(nbt.getFloat("matter_capacity"));
        setMaxReceived(nbt.getFloat("matter_receive"));
        setMaxExtract(nbt.getFloat("matter_extract"));
    }

    public void update(MatterStorage updatedMatterStorage) {
        this.matter = updatedMatterStorage.getMatterStored();
        this.capacity = updatedMatterStorage.getMaxMatterStored();
        this.maxReceive = updatedMatterStorage.getMaxReceived();
        this.maxExtract = updatedMatterStorage.getMaxExtract();

        onMatterChange();
    }
}
