package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.structure;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config.FeatureConfigBuilder;
import io.github.vampirestudios.artifice.api.resource.JsonResource;
import io.github.vampirestudios.artifice.api.util.Processor;

public class ConfiguredStructureFeatureBuilder extends TypedJsonObject {
	public ConfiguredStructureFeatureBuilder() {
		super(new JsonObject());
	}

	public ConfiguredStructureFeatureBuilder type(String type) {
		this.root.addProperty("type", type);
		return this;
	}

	public ConfiguredStructureFeatureBuilder singleBiome(String biome) {
		this.root.addProperty("biomes", biome);
		return this;
	}

	public ConfiguredStructureFeatureBuilder biomeTag(String tag) {
		this.root.addProperty("biomes", tag);
		return this;
	}

	public ConfiguredStructureFeatureBuilder multipleBiomes(String... biomes) {
		JsonArray biomesArray = new JsonArray();
		for (String s : biomes) {
			biomesArray.add(s);
		}
		this.root.add("biomes", biomesArray);
		return this;
	}

	/*
	* Weather or not it should add extra terrain below the structure.
	* */
	public ConfiguredStructureFeatureBuilder adoptNoise(boolean adoptNoise) {
		this.root.addProperty("adoptNoise", adoptNoise);
		return this;
	}

	public ConfiguredStructureFeatureBuilder spawnOverrides(SpawnOverridesBuilder processor) {
		join("spawn_overrides", processor.getData());
		return this;
	}

	public ConfiguredStructureFeatureBuilder featureConfig(FeatureConfigBuilder processor) {
		join("config", processor.getData());
		return this;
	}
}
