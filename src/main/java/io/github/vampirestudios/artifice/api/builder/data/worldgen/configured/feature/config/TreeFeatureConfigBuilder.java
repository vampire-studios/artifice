package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.BlockStateProviderBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.FeatureSizeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.FoliagePlacerBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.TreeDecoratorBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.TrunkPlacerBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;
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

	public <P extends BlockStateProviderBuilder> TreeFeatureConfigBuilder dirtProvider(P instance) {
		with("dirt_provider", JsonObject::new, instance::buildTo);
		return this;
	}

	public <P extends BlockStateProviderBuilder> TreeFeatureConfigBuilder trunkProvider(P instance) {
		with("trunk_provider", JsonObject::new, instance::buildTo);
		return this;
	}

	public <P extends BlockStateProviderBuilder> TreeFeatureConfigBuilder foliageProvider(P instance) {
		with("foliage_provider", JsonObject::new, instance::buildTo);
		return this;
	}

	public <P extends FoliagePlacerBuilder> TreeFeatureConfigBuilder foliagePlacer(P instance) {
		with("foliage_placer", JsonObject::new, instance::buildTo);
		return this;
	}

	public <P extends TrunkPlacerBuilder> TreeFeatureConfigBuilder trunkPlacer(P instance) {
		with("trunk_placer", JsonObject::new, instance::buildTo);
		return this;
	}

	public <P extends FeatureSizeBuilder> TreeFeatureConfigBuilder minimumSize(P instance) {
		with("minimum_size", JsonObject::new, instance::buildTo);
		return this;
	}

	public <D extends TreeDecoratorBuilder> TreeFeatureConfigBuilder addDecorator(Processor<D> processor, D instance) {
		this.root.getAsJsonArray("decorators").add(processor.process(instance).buildTo(new JsonObject()));
		return this;
	}

	public TreeFeatureConfigBuilder heightmap(Heightmap.Types type) {
		this.root.addProperty("heightmap", type.getSerializationKey());
		return this;
	}
}
