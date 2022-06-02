package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import io.github.vampirestudios.artifice.api.builder.data.worldgen.BlockStateProviderBuilder;

public class HugeMushroomFeatureConfigBuilder extends FeatureConfigBuilder {

    public HugeMushroomFeatureConfigBuilder() {
        super();
    }

    public <P extends BlockStateProviderBuilder> HugeMushroomFeatureConfigBuilder capProvider(P processor) {
        join("cap_provider", processor.build());
        return this;
    }

    public <P extends BlockStateProviderBuilder> HugeMushroomFeatureConfigBuilder stemProvider(P processor) {
        join("stem_provider", processor.build());
        return this;
    }

    public HugeMushroomFeatureConfigBuilder foliageRadius(int foliageRadius) {
        this.root.addProperty("foliage_radius", foliageRadius);
        return this;
    }
}
