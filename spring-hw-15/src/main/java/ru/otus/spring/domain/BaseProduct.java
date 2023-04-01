package ru.otus.spring.domain;

import org.apache.commons.lang3.RandomUtils;

public abstract class BaseProduct {

    protected static final float deviationCoef = .05f;

    protected final float standardWeight;
    protected final float weight;

    protected BaseProduct(float standardWeight) {
        this.standardWeight = standardWeight;
        this.weight = standardWeight * RandomUtils.nextFloat(1 - deviationCoef, 1 + deviationCoef);
    }

    public float getStandardWeight() {
        return standardWeight;
    }

    public float getWeight() {
        return weight;
    }
}
