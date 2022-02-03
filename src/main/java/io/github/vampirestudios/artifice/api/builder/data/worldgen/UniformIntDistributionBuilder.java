package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;

public class UniformIntDistributionBuilder extends TypedJsonBuilder<JsonObject> {
    public UniformIntDistributionBuilder() {
        super(new JsonObject(), j->j);
    }

    public UniformIntDistributionBuilder base(int base) {
        this.root.addProperty("base", base);
        return this;
    }

    public UniformIntDistributionBuilder spread(int spread) {
        this.root.addProperty("spread", spread);
        return this;
    }
}
