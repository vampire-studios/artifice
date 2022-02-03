package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;

public class FeatureConfigBuilder extends TypedJsonBuilder<JsonObject> {

    public FeatureConfigBuilder() {
        super(new JsonObject(), j->j);
    }
}
