package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonArray;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.BlockStateProviderBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.FeatureSizeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.FoliagePlacerBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.TreeDecoratorBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.TrunkPlacerBuilder;
import net.minecraft.world.level.levelgen.Heightmap;

public class TreeFeatureConfigBuilder extends FeatureConfigBuilder {

	public TreeFeatureConfigBuilder() {
		super();
		this.root.add("decorators", new JsonArray());
	}

	public TreeFeatureConfigBuilder maxWaterDepth(int maxWaterDepth) {
		this.root.addProperty("max_water_depth", maxWaterDepth);
		return this;
	}

	public TreeFeatureConfigBuilder ignoreVines(boolean ignoreVines) {
		this.root.addProperty("ignore_vines", ignoreVines);
		return this;
	}

    public <P extends BlockStateProviderBuilder> TreeFeatureConfigBuilder dirtProvider(P providerProcessor) {
        join("dirt_provider", providerProcessor.build());
        return this;
    }

    public <P extends BlockStateProviderBuilder> TreeFeatureConfigBuilder trunkProvider(P providerProcessor) {
        join("trunk_provider", providerProcessor.build());
        return this;
    }

    public <P extends BlockStateProviderBuilder> TreeFeatureConfigBuilder foliageProvider(P providerProcessor) {
        join("foliage_provider", providerProcessor.build());
        return this;
    }

    public <P extends FoliagePlacerBuilder> TreeFeatureConfigBuilder foliagePlacer(P providerProcessor) {
        join("foliage_placer", providerProcessor.build());
        return this;
    }

    public <P extends TrunkPlacerBuilder> TreeFeatureConfigBuilder trunkPlacer(P providerProcessor) {
        join("trunk_placer", providerProcessor.build());
        return this;
    }

    public <P extends FeatureSizeBuilder> TreeFeatureConfigBuilder minimumSize(P providerProcessor) {
        join("minimum_size", providerProcessor.build());
        return this;
    }

    public <D extends TreeDecoratorBuilder> TreeFeatureConfigBuilder addDecorator(D processor) {
        this.root.getAsJsonArray("decorators").add(processor.build());
        return this;
    }

	public TreeFeatureConfigBuilder heightmap(Heightmap.Types type) {
		this.root.addProperty("heightmap", type.getSerializationKey());
		return this;
	}
}
