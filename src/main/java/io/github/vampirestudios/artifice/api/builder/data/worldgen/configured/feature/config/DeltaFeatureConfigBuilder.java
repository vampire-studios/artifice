package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.UniformIntDistributionBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class DeltaFeatureConfigBuilder extends FeatureConfigBuilder {

    public DeltaFeatureConfigBuilder() {
        super();
    }

    public DeltaFeatureConfigBuilder size(UniformIntDistributionBuilder processor) {
        join("size", processor.getData());
        return this;
    }

    public DeltaFeatureConfigBuilder rimSize(UniformIntDistributionBuilder processor) {
        join("rim_size", processor.getData());
        return this;
    }

    public DeltaFeatureConfigBuilder rim(StateDataBuilder processor) {
        join("rim", processor.getData());
        return this;
    }

    public DeltaFeatureConfigBuilder contents(StateDataBuilder processor) {
        join("contents", processor.getData());
        return this;
    }
}
