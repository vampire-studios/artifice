package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config.FeatureConfigBuilder;
import io.github.vampirestudios.artifice.api.resource.JsonResource;
import io.github.vampirestudios.artifice.api.util.Processor;

public class ConfiguredFeatureBuilder extends TypedJsonObject<JsonResource<JsonObject>> {
    public ConfiguredFeatureBuilder() {
        super(new JsonObject(), JsonResource::new);
    }

    public ConfiguredFeatureBuilder featureID(String id) {
        this.root.addProperty("type", id);
        return this;
    }

    public <C extends FeatureConfigBuilder> ConfiguredFeatureBuilder featureConfig(Processor<C> processor, C instance) {
        with("config", JsonObject::new, jsonObject -> processor.process(instance).buildTo(jsonObject));
        return this;
    }

    public ConfiguredFeatureBuilder defaultConfig() {
        return this.featureConfig(featureConfigBuilder -> {} , new FeatureConfigBuilder());
    }
}
