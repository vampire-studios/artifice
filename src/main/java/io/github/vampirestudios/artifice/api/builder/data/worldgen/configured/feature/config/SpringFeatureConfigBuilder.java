package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class SpringFeatureConfigBuilder extends FeatureConfigBuilder {

    public SpringFeatureConfigBuilder() {
        super();
        this.root.add("valid_blocks", new JsonArray());
    }

    public SpringFeatureConfigBuilder fluidState(StateDataBuilder processor) {
        join("state", processor.buildCopy());
        return this;
    }

    public SpringFeatureConfigBuilder addValidBlock(String blockID) {
        this.root.getAsJsonArray("valid_blocks").add(blockID);
        return this;
    }

    public SpringFeatureConfigBuilder requiresBlockBelow(boolean requiresBlockBelow) {
        this.root.addProperty("requires_block_below", requiresBlockBelow);
        return this;
    }

    public SpringFeatureConfigBuilder rockCount(int rockCount) {
        this.root.addProperty("rock_count", rockCount);
        return this;
    }

    public SpringFeatureConfigBuilder holeCount(int holeCount) {
        this.root.addProperty("hole_count", holeCount);
        return this;
    }
}
