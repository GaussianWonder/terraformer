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

    public void setMatter(float matter) {
        this.matter = matter;
    }

    @Override
    public float receiveMatter(float maxReceive, boolean simulate) {
        if (!canReceive())
            return 0.0f;

        float matterReceived = Math.min(capacity - matter, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            matter += matterReceived;
        return matterReceived;
    }

    @Override
    public float extractMatter(float maxExtract, boolean simulate) {
        if (!canExtract())
            return 0.0f;

        float matterExtracted = Math.min(matter, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            matter -= matterExtracted;
        return matterExtracted;
    }

    @Override
    public float getMatterStored() {
        return matter;
    }

    @Override
    public float getMaxMatterStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putFloat("matter", getMatterStored());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setMatter(nbt.getFloat("matter"));
    }
}
