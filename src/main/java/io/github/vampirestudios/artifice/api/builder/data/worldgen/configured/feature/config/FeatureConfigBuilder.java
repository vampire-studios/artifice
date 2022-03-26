package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;

public class FeatureConfigBuilder extends TypedJsonObject<JsonObject> {
    public FeatureConfigBuilder() {
        super(new JsonObject(), j->j);
    }
}
