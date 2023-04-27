package dev.totaltax.graviton.module.setting;

public class Setting {
    private final String name, description;

    public Setting(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return description;
    }

    public static class Builder {
        private String name, description;
        public Builder() {}

        public static Builder newInstance()
        {
            return new Builder();
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Setting build() {
            return new Setting(this);
        }
    }
}
