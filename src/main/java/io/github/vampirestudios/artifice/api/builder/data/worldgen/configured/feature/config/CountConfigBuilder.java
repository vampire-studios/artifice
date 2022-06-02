package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import io.github.vampirestudios.artifice.api.builder.data.worldgen.UniformIntDistributionBuilder;

public class CountConfigBuilder extends FeatureConfigBuilder {
    public CountConfigBuilder() {
        super();
    }

    public CountConfigBuilder count(int count) {
        this.root.addProperty("count", count);
        return this;
    }

    public CountConfigBuilder count(UniformIntDistributionBuilder processor) {
        join("count", processor.build());
        return this;
    }
}
