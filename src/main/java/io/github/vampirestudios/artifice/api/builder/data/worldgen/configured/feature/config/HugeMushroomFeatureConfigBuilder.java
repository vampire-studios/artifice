package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.BlockStateProviderBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class HugeMushroomFeatureConfigBuilder extends FeatureConfigBuilder {

    public HugeMushroomFeatureConfigBuilder() {
        super();
    }

    public <P extends BlockStateProviderBuilder> HugeMushroomFeatureConfigBuilder capProvider(P processor) {
        join("cap_provider", processor.getData());
        return this;
    }

    public <P extends BlockStateProviderBuilder> HugeMushroomFeatureConfigBuilder stemProvider(P processor) {
        join("stem_provider", processor.getData());
        return this;
    }

    public HugeMushroomFeatureConfigBuilder foliageRadius(int foliageRadius) {
        this.root.addProperty("foliage_radius", foliageRadius);
        return this;
    }
}
