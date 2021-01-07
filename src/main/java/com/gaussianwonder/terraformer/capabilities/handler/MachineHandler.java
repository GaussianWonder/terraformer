package com.gaussianwonder.terraformer.capabilities.handler;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class MachineHandler implements IMachineHandler, INBTSerializable<CompoundNBT> {
    protected int cooldown; // baseStats.speed is the baseCooldown
    protected Stats baseStats; // initial Stats of the machine that the upgrades stack up onto
    protected Stats upgradedStats; // Stats that determine upgrades only

    // These should be calculated by recalculateStats() using baseStats and upgradedStats
    // Do not Sync these with the client, ask it to calculate them instead
    protected int maxCooldown;
    protected float speedProductionFactor;
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
        this.speedProductionFactor = calculateSpeedProductionFactor();
        this.outputProductionFactor = calculateOutputProductionFactor();
        this.inputSupplyFactor = calculateInputSupplyFactor();

        this.maxCooldown = calculateCooldownTicks();

        onStatsChange();
    }

    public void onStatsChange() { }

    @Override
    public int calculateCooldownTicks() {
        // you can spam speed upgrades, but you won't get anything out of it
        // however you can go the other way around, make it go slow and benefit from bonus output production

        return (int)(speedProductionFactor * baseStats.speed);
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
    public float calculateSpeedProductionFactor() {
        // best speed x0.3
        // default x1.3
        // worst -infinity (but you get output reward)
        return (float) (0.3F + (1F / Math.pow(Math.E, (double)upgradedStats.speed / 30F)));
    }

    @Override
    public float calculateOutputProductionFactor() {
        // decrease the output production when going fast, increase it when going slow
        return speedProductionFactor * (baseStats.output + (
                (float)(1F - 1F / Math.pow(Math.E, (double)upgradedStats.output / 30F)) * 1.2F
                ));

        // 20% upgrade controlled
        // 80% speed controlled
    }

    @Override
    public float calculateInputSupplyFactor() {
        // "speed" can be achieved if input is high enough, what i want to dodge is a scenario in which
        // output + input upgrades are way too convenient, since you can't have output with speed
        // so input will be directly proportional with speed, and the max limit will be controlled by base stats
        return baseStats.input + Math.min(baseStats.input, upgradedStats.input/100F)/speedProductionFactor;
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
            case INPUT:
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
        float speedRatio = (float)baseStats.speed / maxCooldown;
        float outputRatio = outputProductionFactor;
        float inputRatio = inputSupplyFactor;

        float totalRatio = speedRatio + outputRatio + inputRatio;
        return new StatsRatio(
                speedRatio / totalRatio,
                outputRatio / totalRatio,
                inputRatio / totalRatio
        );
    }
}