package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.BlockStateProviderBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.BlockPlacerBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class RandomPatchFeatureConfigBuilder extends FeatureConfigBuilder {

    public RandomPatchFeatureConfigBuilder() {
        super();
        this.root.add("whitelist", new JsonArray());
        this.root.add("blacklist", new JsonArray());
    }

    public <P extends BlockStateProviderBuilder> RandomPatchFeatureConfigBuilder stateProvider(P processor, P instance) {
        join("state_provider", processor.getData());
        return this;
    }

    public <P extends BlockPlacerBuilder> RandomPatchFeatureConfigBuilder blockPlacer(P processor, P instance) {
        join("block_placer", processor.getData());
        return this;
    }

    public RandomPatchFeatureConfigBuilder addBlockToWhitelist(StateDataBuilder processor) {
        this.root.getAsJsonArray("whitelist").add(processor.getData());
        return this;
    }

    public RandomPatchFeatureConfigBuilder addBlockToBlacklist(StateDataBuilder processor) {
        this.root.getAsJsonArray("blacklist").add(processor.getData());
        return this;
    }

    public RandomPatchFeatureConfigBuilder tries(int tries) {
        this.root.addProperty("tries", tries);
        return this;
    }

    public RandomPatchFeatureConfigBuilder xSpread(int xSpread) {
        this.root.addProperty("xspread", xSpread);
        return this;
    }

    public RandomPatchFeatureConfigBuilder ySpread(int ySpread) {
        this.root.addProperty("yspread", ySpread);
        return this;
    }

    public RandomPatchFeatureConfigBuilder zSpread(int zSpread) {
        this.root.addProperty("zspread", zSpread);
        return this;
    }

    public RandomPatchFeatureConfigBuilder canReplace(boolean canReplace) {
        this.root.addProperty("can_replace", canReplace);
        return this;
    }

    public RandomPatchFeatureConfigBuilder project(boolean project) {
        this.root.addProperty("project", project);
        return this;
    }

    public RandomPatchFeatureConfigBuilder needWater(boolean needWater) {
        this.root.addProperty("need_water", needWater);
        return this;
    }
}
