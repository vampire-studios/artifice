package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.BlockStateProviderBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class HugeMushroomFeatureConfigBuilder extends FeatureConfigBuilder {

	public HugeMushroomFeatureConfigBuilder() {
		super();
	}

	public <P extends BlockStateProviderBuilder> HugeMushroomFeatureConfigBuilder capProvider(Processor<P> processor, P instance) {
		with("cap_provider", JsonObject::new, jsonObject -> processor.process(instance).buildTo(jsonObject));
		return this;
	}

	public <P extends BlockStateProviderBuilder> HugeMushroomFeatureConfigBuilder stemProvider(Processor<P> processor, P instance) {
		with("stem_provider", JsonObject::new, jsonObject -> processor.process(instance).buildTo(jsonObject));
		return this;
	}

	public HugeMushroomFeatureConfigBuilder foliageRadius(int foliageRadius) {
		this.root.addProperty("foliage_radius", foliageRadius);
		return this;
	}
}
