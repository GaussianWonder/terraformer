package com.gaussianwonder.terraformer.setup.capabilities.storage;

import com.gaussianwonder.terraformer.setup.capabilities.i_storage.IMatterStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class MatterStorage implements IMatterStorage, INBTSerializable<CompoundNBT> {
    protected float matter;
    protected float capacity;
    protected float maxReceive;
    protected float maxExtract;

    public MatterStorage(float capacity)
    {
        this(capacity, capacity, capacity, 0.0f);
    }

    public MatterStorage(float capacity, float maxTransfer)
    {
        this(capacity, maxTransfer, maxTransfer, 0.0f);
    }

    public MatterStorage(float capacity, float maxReceive, float maxExtract)
    {
        this(capacity, maxReceive, maxExtract, 0.0f);
    }

    public MatterStorage(float capacity, float maxReceive, float maxExtract, float matter)
    {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.matter = Math.max(0 , Math.min(capacity, matter));
    }

    protected void onMatterChange() { }

    public float receiveMatter(float maxReceive, boolean simulate) {
        if (!canReceive())
            return 0.0f;

        float matterReceived = Math.min(capacity - matter, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            matter += matterReceived;

        onMatterChange();
        return matterReceived;
    }

    public float extractMatter(float maxExtract, boolean simulate) {
        if (!canExtract())
            return 0.0f;

        float matterExtracted = Math.min(matter, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            matter -= matterExtracted;

        onMatterChange();
        return matterExtracted;
    }

    public float getMatterStored() {
        return matter;
    }

    public float getMaxMatterStored() {
        return capacity;
    }

    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    public boolean canReceive() {
        return this.maxReceive > 0;
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

    public void setMatter(float matter) {
        this.matter = matter;
        onMatterChange();
    }

    public void setMaxMatterStored(float capacity) {
        this.capacity = capacity;
        onMatterChange();
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();

        tag.putFloat("matter_stored", getMatterStored());
        tag.putFloat("matter_capacity", getMaxMatterStored());
        tag.putFloat("matter_receive", getMaxReceived());
        tag.putFloat("matter_extract", getMaxExtract());

        return tag;
    }

    public void deserializeNBT(CompoundNBT nbt) {
        setMatter(nbt.getFloat("matter_stored"));
        setMaxMatterStored(nbt.getFloat("matter_capacity"));
        setMaxReceived(nbt.getFloat("matter_receive"));
        setMaxExtract(nbt.getFloat("matter_extract"));
    }
}
