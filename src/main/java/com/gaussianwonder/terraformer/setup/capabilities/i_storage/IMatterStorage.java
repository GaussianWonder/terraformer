package com.gaussianwonder.terraformer.setup.capabilities.i_storage;

public interface IMatterStorage {
    /**
     * Adds matter to the storage. Returns quantity of matter that was accepted.
     *
     * @param maxReceive
     *            Maximum amount of matter to be inserted.
     * @param simulate
     *            If TRUE, the insertion will only be simulated.
     * @return Amount of matter that was (or would have been, if simulated) accepted by the storage.
     */
    float receiveMatter(float maxReceive, boolean simulate);

    /**
     * Removes matter from the storage. Returns quantity of matter that was removed.
     *
     * @param maxExtract
     *            Maximum amount of matter to be extracted.
     * @param simulate
     *            If TRUE, the extraction will only be simulated.
     * @return Amount of matter that was (or would have been, if simulated) extracted from the storage.
     */
    float extractMatter(float maxExtract, boolean simulate);

    /**
     * Returns the amount of matter currently stored.
     */
    float getMatterStored();

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
    void setMatter(float matter);
    void setMaxMatterStored(float capacity);
    void setMaxReceived(float maxReceived);
    void setMaxExtract(float maxExtract);
}
