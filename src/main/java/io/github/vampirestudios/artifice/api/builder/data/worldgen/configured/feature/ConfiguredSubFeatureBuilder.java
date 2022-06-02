package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config.FeatureConfigBuilder;

public class ConfiguredSubFeatureBuilder extends TypedJsonObject {
    public ConfiguredSubFeatureBuilder() {
        super(new JsonObject());
    }

    public ConfiguredSubFeatureBuilder featureID(String id) {
        this.root.addProperty("type", id);
        return this;
    }

    public <C extends FeatureConfigBuilder> ConfiguredSubFeatureBuilder featureConfig(C instance) {
        join("config", instance.build());
        return this;
    }

    public ConfiguredSubFeatureBuilder defaultConfig() {
        return this.featureConfig(new FeatureConfigBuilder());
    }
}
