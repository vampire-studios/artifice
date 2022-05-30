package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.decorator.ConfiguredDecoratorBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.ConfiguredSubFeatureBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class DecoratedFeatureConfigBuilder extends FeatureConfigBuilder {

    public DecoratedFeatureConfigBuilder() {
        super();
    }

    public DecoratedFeatureConfigBuilder feature(ConfiguredSubFeatureBuilder processor) {
        join("feature", processor.getData());
        return this;
    }

    public DecoratedFeatureConfigBuilder feature(String configuredFeatureID) {
        this.root.addProperty("feature", configuredFeatureID);
        return this;
    }

    public DecoratedFeatureConfigBuilder decorator(ConfiguredDecoratorBuilder processor) {
        join("decorator", processor.getData());
        return this;
    }

    public DecoratedFeatureConfigBuilder decorator(String configuredDecoratorID) {
        this.root.addProperty("decorator", configuredDecoratorID);
        return this;
    }
}
