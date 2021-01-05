package com.gaussianwonder.terraformer.setup.capabilities.handler;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class MachineHandler implements IMachineHandler, INBTSerializable<CompoundNBT> {
    protected int cooldown; // baseStats.speed is the baseCooldown
    protected Stats baseStats; // initial Stats of the machine that the upgrades stack up onto
    protected Stats upgradedStats; // Stats that determine upgrades only

    // These should be calculated by recalculateStats() using baseStats and upgradedStats
    // Do not Sync these with the client, ask it to calculate them instead
    protected int maxCooldown;
    protected float speedProductionFactor; // this acts more as a constant for inner logic, but you can totally use it
    protected float outputProductionFactor; // this is here for you to multiply when adding to the MatterStore
    protected float inputSupplyFactor; // this is here for you to multiply the number of items you extract to generate Matter

    public MachineHandler() {
        this.baseStats = new Stats(20, 1, 1);
        this.upgradedStats = new Stats(0, 0, 0);

        recalculateStats();
    }

    public MachineHandler(Stats baseStats) {
        this.cooldown = 0;
        this.baseStats = baseStats;
        this.upgradedStats = new Stats(0, 0, 0);

        recalculateStats();
    }

    public MachineHandler(Stats baseStats, Stats upgradedStats) {
        this.cooldown = 0;
        this.baseStats = baseStats;
        this.upgradedStats = upgradedStats;

        recalculateStats();
    }

    @Override
    public boolean isBusy() {
        return cooldown < maxCooldown;
    }

    @Override
    public void busy() {
        reset();
    }

    @Override
    public void recalculateStats() {
        this.maxCooldown = calculateCooldownTicks();
        this.speedProductionFactor = calculateSpeedProductionFactor();
        this.outputProductionFactor = calculateOutputProductionFactor();
        this.inputSupplyFactor = calculateInputSupplyFactor();

        onStatsChange();
    }

    public void onStatsChange() { }

    @Override
    public int calculateCooldownTicks() {
        // you can spam speed upgrades, but you won't get anything out of it
        // however you can go the other way around, make it go slow and benefit from bonus output production
        double efficiency = upgradedStats.speed;
        double lowerBound = baseStats.speed/2.0f;
        double plot = (double)(2* baseStats.speed) / Math.pow(Math.E, efficiency / baseStats.speed);

        return (int)(lowerBound + plot);
    }

    @Override
    public void tick() {
        ++cooldown;
        onStatsChange();
    }

    @Override
    public void reset() {
        cooldown = 0;
        onStatsChange();
    }

    @Override
    public float calculateOutputProductionFactor() {
        // decrease the output production when going fast
        return baseStats.output/speedProductionFactor + upgradedStats.output;
    }

    @Override
    public float calculateInputSupplyFactor() {
        // make it suck more items in when going slow, but with diminishing effects,
        // such that speed upgrades still make sense
        return baseStats.input*(outputProductionFactor * 0.80f) + upgradedStats.input/(speedProductionFactor * 0.80f);
    }

    @Override
    public float calculateSpeedProductionFactor() {
        return ((float)baseStats.speed+2) / maxCooldown;
    }

    @Override
    public void change(Target target, int value) {
        switch (target) {
            case SPEED:
                upgradedStats.speed += value;
                break;
            case OUTPUT:
                upgradedStats.output += value;
                break;
            case SUPPLY:
                upgradedStats.input += value;
                break;
        }
        recalculateStats();
    }

    @Override
    public void setBaseStats(Stats baseStats) {
        this.baseStats = baseStats;
        recalculateStats();
    }

    @Override
    public void setUpgradedStats(Stats upgradedStats) {
        this.upgradedStats = upgradedStats;
        recalculateStats();
    }

    @Override
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public float getOutputProductionFactor() {
        return outputProductionFactor;
    }

    @Override
    public float getInputSupplyFactor() {
        return inputSupplyFactor;
    }

    @Override
    public float getSpeedProductionFactor() {
        return speedProductionFactor;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();

        tag.putInt("cooldown", cooldown);

        tag.putInt("base_speed", baseStats.speed);
        tag.putInt("base_output", baseStats.output);
        tag.putInt("base_input", baseStats.input);

        tag.putInt("upgrade_speed", upgradedStats.speed);
        tag.putInt("upgrade_output", upgradedStats.output);
        tag.putInt("upgrade_input", upgradedStats.input);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.cooldown = nbt.getInt("cooldown");

        this.baseStats = new Stats(
                nbt.getInt("base_speed"),
                nbt.getInt("base_output"),
                nbt.getInt("base_input")
        );

        this.upgradedStats = new Stats(
                nbt.getInt("upgrade_speed"),
                nbt.getInt("upgrade_output"),
                nbt.getInt("upgrade_input")
        );

        recalculateStats();
    }

    public int getCooldown() {
        return cooldown;
    }

    public Stats getBaseStats() {
        return this.baseStats;
    }

    public Stats getUpgradedStats() {
        return this.upgradedStats;
    }

    public StatsRatio getRatio() {
        float totalFactor = speedProductionFactor + outputProductionFactor + inputSupplyFactor;

        float speedRatio = speedProductionFactor/totalFactor;
        float outputRatio = outputProductionFactor/totalFactor;
        float inputRatio = inputSupplyFactor/totalFactor;

        return new StatsRatio(
                speedRatio,
                outputRatio,
                inputRatio
        );
    }
}