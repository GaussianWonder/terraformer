package com.gaussianwonder.terraformer.capabilities.storage;

public interface IMatterStorage {
    class Matter {
        public float solid;
        public float soft;
        public float granular;

        public Matter() {
            this.solid = 0;
            this.soft = 0;
            this.granular = 0;
        }

        public Matter(float solid, float soft, float granular) {
            this.solid = Math.max(0, solid);
            this.soft = Math.max(0, soft);
            this.granular = Math.max(0, granular);
        }

        public Matter(Matter matter, float capacity) {
            Matter capped = Matter.capByCapacity(matter, capacity);

            this.solid = capped.solid;
            this.soft = capped.soft;
            this.granular = capped.granular;
        }

        public Matter(Matter matter) {
            this.solid = matter.solid;
            this.soft = matter.soft;
            this.granular = matter.granular;
        }

        float getMatter() {
            return this.solid + this.soft + this.granular;
        }

        public static Matter capByCapacity(Matter matter, float capacity) {
            float totalCapacity = matter.getMatter();
            if(totalCapacity > capacity) {
                int nonZero = Float.compare(matter.solid, 0.0f) +
                        Float.compare(matter.soft, 0.0f) +
                        Float.compare(matter.granular, 0.0f);

                float cutOff = (totalCapacity - capacity) / nonZero;

                return new Matter(
                        matter.solid - cutOff * Float.compare(matter.solid, 0.0f),
                        matter.soft - cutOff * Float.compare(matter.soft, 0.0f),
                        matter.granular - cutOff * Float.compare(matter.granular, 0.0f)
                );
            }
            return matter;
        }

        public void add(Matter matter) {
            solid += matter.solid;
            soft += matter.soft;
            granular += matter.granular;
        }

        public void substract(Matter matter) {
            solid -= matter.solid;
            soft -= matter.soft;
            granular -= matter.granular;
        }

        public static Matter add(Matter m1, Matter m2) {
            return new Matter(
                    m1.solid + m2.solid,
                    m1.soft + m2.soft,
                    m1.granular + m2.granular
            );
        }

        public static Matter substract(Matter m1, Matter m2) {
            return new Matter(
                    m1.solid - m2.solid,
                    m1.soft - m2.soft,
                    m1.granular - m2.granular
            );
        }
    }

    /**
     * Adds matter to the storage. Returns quantity of matter that was accepted.
     *
     * @param maxReceive
     *            Maximum amount of matter to be inserted.
     * @param simulate
     *            If TRUE, the insertion will only be simulated.
     * @return Amount of matter that was (or would have been, if simulated) accepted by the storage.
     */
    Matter receiveMatter(Matter maxReceive, boolean simulate);

    /**
     * Removes matter from the storage. Returns quantity of matter that was removed.
     *
     * @param maxExtract
     *            Maximum amount of matter to be extracted.
     * @param simulate
     *            If TRUE, the extraction will only be simulated.
     * @return Amount of matter that was (or would have been, if simulated) extracted from the storage.
     */
    Matter extractMatter(Matter maxExtract, boolean simulate);

    /**
     * Returns the amount of matter currently stored.
     */
    Matter getMatterStored();

    /**
     * Returns the maximum amount of matter that can be stored.
     */
    float getMaxMatterStored();

    /**
     * Returns if this storage can have matter extracted.
     * If this is false, then any calls to extractMatter will return 0.0f.
     */
    boolean canExtract();

    /**
     * Used to determine if this storage can receive matter.
     * If this is false, then any calls to receiveMatter will return 0.0f.
     */
    boolean canReceive();

    float getMaxReceived();
    float getMaxExtract();
    void setMatter(Matter matter);
    void setMaxMatterStored(float capacity);
    void setMaxReceived(float maxReceived);
    void setMaxExtract(float maxExtract);
}
