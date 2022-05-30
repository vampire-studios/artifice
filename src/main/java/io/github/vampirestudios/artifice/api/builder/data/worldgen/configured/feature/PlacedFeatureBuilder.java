package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config.FeatureConfigBuilder;
import io.github.vampirestudios.artifice.api.resource.JsonResource;
import io.github.vampirestudios.artifice.api.util.Processor;

public class PlacedFeatureBuilder extends TypedJsonObject {
    public PlacedFeatureBuilder() {
        super(new JsonObject());
    }

    public PlacedFeatureBuilder featureID(String id) {
        this.root.addProperty("feature", id);
        return this;
    }

    public <C extends FeatureConfigBuilder> PlacedFeatureBuilder featureConfig(C processor) {
        join("config", processor.getData());
        return this;
    }

    public PlacedFeatureBuilder defaultConfig() {
        return this.featureConfig(new FeatureConfigBuilder());
    }
}
