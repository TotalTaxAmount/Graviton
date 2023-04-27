package dev.totaltax.graviton.module.setting.impl;

import dev.totaltax.graviton.module.setting.Setting;

import static net.minecraft.util.Mth.clamp;

public class NumberSetting extends Setting {

    private final double defaultValue, increment, min, max;
    private double value;
    public NumberSetting(Builder builder) {
        super(builder);
        this.defaultValue = builder.defaultValue;
        this.increment = builder.increment;
        this.min = builder.min;
        this.max = builder.max;
    }

    public double getDefaultValue() {
        return defaultValue;
    }

    public double getIncrement() {
        return increment;
    }

    public double getValue() {
        return value;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public void setValue(double value) {
        value = clamp(value, this.min, this.max);
        value = Math.round(value * (1.0 / this.increment)) / (1.0 / this.increment);
        this.value = value;
    }

    public void increment(boolean positive) {
        if (positive) {
            setValue(getValue() + getIncrement());
        } else {
            setValue(getValue() - getIncrement());
        }
    }

    public static class Builder extends Setting.Builder {
        private double defaultValue, increment, min, max;


        public static Builder newInstance() {
            return new Builder();
        }
        public Builder setDefault(double defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder setIncrement(double increment) {
            this.increment = increment;
            return this;
        }

        public Builder setMin(double min) {
            this.min = min;
            return this;
        }

        public Builder setMax(double max) {
            this.max = max;
            return this;
        }

        @Override
        public NumberSetting build() {
            return new NumberSetting(this);
        }
    }
}
