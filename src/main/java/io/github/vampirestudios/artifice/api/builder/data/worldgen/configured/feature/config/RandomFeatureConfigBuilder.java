package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;

public class RandomFeatureConfigBuilder extends FeatureConfigBuilder {

    public RandomFeatureConfigBuilder() {
        super();
        this.root.add("features", new JsonArray());
    }

    public RandomFeatureConfigBuilder defaultConfiguredFeature(String configuredFeatureID) {
        this.root.addProperty("default", configuredFeatureID);
        return this;
    }

    public RandomFeatureConfigBuilder addConfiguredFeature(RandomFeatureEntryBuilder processor) {
        this.root.getAsJsonArray("features").add(processor.build());
        return this;
    }

    public static class RandomFeatureEntryBuilder extends TypedJsonObject {

        public RandomFeatureEntryBuilder() {
            super(new JsonObject());
        }

        public RandomFeatureEntryBuilder chance(float chance) {
            if (chance > 1.0F) throw new IllegalArgumentException("chance can't be higher than 1.0F! Found " + chance);
            if (chance < 0.0F) throw new IllegalArgumentException("chance can't be smaller than 0.0F! Found " + chance);
            this.root.addProperty("chance", chance);
            return this;
        }

        public RandomFeatureEntryBuilder configuredFeatureID(String configuredFeatureID) {
            this.root.addProperty("feature", configuredFeatureID);
            return this;
        }
    }
}
