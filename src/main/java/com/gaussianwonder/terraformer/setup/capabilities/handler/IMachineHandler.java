package com.gaussianwonder.terraformer.setup.capabilities.handler;

/**
 * This capability has everything to do with the configuring aspect of a machine:
 *  cooldown
 *  upgrades
 *  efficienty
 *  ...
 * an it has nothing to do with the actual logic of the machine.
 */
public interface IMachineHandler {
    class Stats {
        public int speed; // how fast the machine is processing, how many ticks it takes for the cooldown to finish
        public int output; // how much to output from a single process
        public int input; // how many inputs to process at once

        public Stats(int speed, int output, int input) {
            this.speed = speed;
            this.output = output;
            this.input = input;
        }
    }

    enum Target {
        SPEED,
        OUTPUT,
        SUPPLY
    }

    /**
     * Generally this should look something like:
     *  return getCooldown() >= calculateCooldownTicks();
     * @return whether or not the machine is busy.
     */
    boolean isBusy();

    /**
     * If not busy, reset
     */
    void reset();
    void busy(); // same thing different name, this might make more sense setBuse();

    /**
     * This should be called after each change()
     */
    void recalculateStats();

    /**
     * This value must be proportional in some way with the speed factor
     * The only reason one might need this information is for GUI sync rate between the client and server
     * @return the current maximum cooldown ticks.
     */
    int calculateCooldownTicks();

    /**
     * Tick the machine once such that the cooldown progresses
     * Note: The actual machine logic should NOT be implemented here
     */
    void tick();

    /**
     * This is generally a value between 1 and whatever.
     * It is the output multiplication factor of whatever the machine is producing.
     * @return the current production factor value
     */
    float calculateOutputProductionFactor();

    /**
     * This is generally a value between 1 and whatever.
     * It is the input supply factor of whatever the machine can process at once.
     * @return the current input supply factor
     */
    float calculateInputSupplyFactor();

    /**
     * This is generally a value between 1 and watever. (i can also be below 1 if slowness is desired)
     * It is the processing speed multiplication factor of whatever the machine can process at once.
     * @return the current speed production factor
     */
    float calculateSpeedProductionFactor();

    /**
     * Change the target according to some value
     * @param target the target production factor to upgrade
     * @param value a reference value to use
     */
    void change(Target target, int value);

    void setBaseStats(Stats baseStats);
    void setUpgradedStats(Stats upgradedStats);
    void setCooldown(int cooldown);

    float getOutputProductionFactor();
    float getInputSupplyFactor();
    float getSpeedProductionFactor();

    int getCooldown();
    Stats getBaseStats();
    Stats getUpgradedStats();
}
