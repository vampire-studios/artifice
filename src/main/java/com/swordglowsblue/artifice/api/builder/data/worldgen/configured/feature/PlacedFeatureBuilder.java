package com.swordglowsblue.artifice.api.builder.data.worldgen.configured.feature;

import com.google.gson.JsonObject;
import com.swordglowsblue.artifice.api.builder.TypedJsonBuilder;
import com.swordglowsblue.artifice.api.builder.data.worldgen.configured.feature.config.FeatureConfigBuilder;
import com.swordglowsblue.artifice.api.resource.JsonResource;
import com.swordglowsblue.artifice.api.util.Processor;

public class PlacedFeatureBuilder extends TypedJsonBuilder<JsonResource<JsonObject>> {
    public PlacedFeatureBuilder() {
        super(new JsonObject(), JsonResource::new);
    }

    public PlacedFeatureBuilder featureID(String id) {
        this.root.addProperty("type", id);
        return this;
    }

    public <C extends FeatureConfigBuilder> PlacedFeatureBuilder featureConfig(Processor<C> processor, C instance) {
        with("config", JsonObject::new, jsonObject -> processor.process(instance).buildTo(jsonObject));
        return this;
    }

    public PlacedFeatureBuilder defaultConfig() {
        return this.featureConfig(featureConfigBuilder -> {} , new FeatureConfigBuilder());
    }
}
