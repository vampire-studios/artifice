package io.github.vampirestudios.artifice.api.builder.data.worldgen.biome;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;

public class BiomeSpawnEntryBuilder extends TypedJsonObject {

    public BiomeSpawnEntryBuilder() {
        super(new JsonObject());
    }

    public BiomeSpawnEntryBuilder entityID(String entityID) {
        this.root.addProperty("type", entityID);
        return this;
    }

    public BiomeSpawnEntryBuilder weight(int weight) {
        this.root.addProperty("weight", weight);
        return this;
    }

    public BiomeSpawnEntryBuilder minCount(int minCount) {
        this.root.addProperty("minCount", minCount);
        return this;
    }

    public BiomeSpawnEntryBuilder maxCount(int maxCount) {
        this.root.addProperty("maxCount", maxCount);
        return this;
    }
}
