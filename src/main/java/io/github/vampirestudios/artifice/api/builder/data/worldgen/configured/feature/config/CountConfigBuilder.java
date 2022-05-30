package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.UniformIntDistributionBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class CountConfigBuilder extends FeatureConfigBuilder {
    public CountConfigBuilder() {
        super();
    }

    public CountConfigBuilder count(int count) {
        this.root.addProperty("count", count);
        return this;
    }

    public CountConfigBuilder count(UniformIntDistributionBuilder processor) {
        join("count", processor.getData());
        return this;
    }
}
