package io.github.vampirestudios.artifice.api.builder.data.dimension;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class ChunkGeneratorTypeBuilder extends TypedJsonBuilder<JsonObject> {

	protected ChunkGeneratorTypeBuilder() {
		super(new JsonObject(), j -> j);
	}

	/**
	 * Set the type (ID) of the Chunk Generator.
	 *
	 * @param type
	 * @param <T>
	 * @return
	 */
	public <T extends ChunkGeneratorTypeBuilder> T type(String type) {
		this.root.addProperty("type", type);
		return (T) this;
	}

	/**
	 * Set the biome Source.
	 *
	 * @param biomeSourceBuilder
	 * @param biomeSourceBuilderInstance
	 * @param <T>
	 * @return
	 */
	public <T extends BiomeSourceBuilder> ChunkGeneratorTypeBuilder biomeSource(Processor<T> biomeSourceBuilder, T biomeSourceBuilderInstance) {
		with("biome_source", JsonObject::new, biomeSource -> biomeSourceBuilder.process(biomeSourceBuilderInstance).buildTo(biomeSource));
		return this;
	}


	public static class NoiseChunkGeneratorTypeBuilder extends ChunkGeneratorTypeBuilder {
		public NoiseChunkGeneratorTypeBuilder() {
			super();
			this.type("minecraft:noise");
		}

		/**
		 * @deprecated use noiseSettings instead.
		 */
		@Deprecated
		public NoiseChunkGeneratorTypeBuilder presetSettings(String presetId) {
			this.noiseSettings(presetId);
			return this;
		}

		/**
		 * Set Noise Settings to Id.
		 *
		 * @param noiseSettingsID the identifier
		 * @return this
		 */
		public NoiseChunkGeneratorTypeBuilder noiseSettings(String noiseSettingsID) {
			this.root.addProperty("settings", noiseSettingsID);
			return this;
		}

		/**
		 * Build a multi-noise biome source.
		 *
		 * @return this
		 */
		public NoiseChunkGeneratorTypeBuilder multiNoiseBiomeSource(Processor<BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder> biomeSourceBuilder) {
			biomeSource(biomeSourceBuilder, new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder());
			return this;
		}

		/**
		 * Build a checkerboard biome source.
		 *
		 * @return this
		 */
		public NoiseChunkGeneratorTypeBuilder checkerboardBiomeSource(Processor<BiomeSourceBuilder.CheckerboardBiomeSourceBuilder> biomeSourceBuilder) {
			biomeSource(biomeSourceBuilder, new BiomeSourceBuilder.CheckerboardBiomeSourceBuilder());
			return this;
		}

		/**
		 * Build a fixed biome source.
		 *
		 * @return this
		 */
		public NoiseChunkGeneratorTypeBuilder fixedBiomeSource(Processor<BiomeSourceBuilder.FixedBiomeSourceBuilder> biomeSourceBuilder) {
			biomeSource(biomeSourceBuilder, new BiomeSourceBuilder.FixedBiomeSourceBuilder());
			return this;
		}

		/**
		 * Build a simple biome source.
		 *
		 * @param id the identifier of the biome source
		 * @return this
		 */
		public NoiseChunkGeneratorTypeBuilder simpleBiomeSource(String id) {
			this.root.addProperty("biome_source", id);
			return this;
		}
	}

	public static class FlatChunkGeneratorTypeBuilder extends ChunkGeneratorTypeBuilder {
		public FlatChunkGeneratorTypeBuilder() {
			super();
			this.type("minecraft:flat");
			this.root.add("settings", new JsonObject());
		}

		/**
		 * Set the biome to biomeId.
		 *
		 * @param biomeId the biome id.
		 * @return this
		 */
		public FlatChunkGeneratorTypeBuilder biome(String biomeId) {
			this.root.getAsJsonObject("settings").addProperty("biome", biomeId);
			return this;
		}

		/**
		 * Defines if the generator should generate lakes  or not
		 *
		 * @return this
		 */
		public FlatChunkGeneratorTypeBuilder lakes(boolean lakes) {
			this.root.getAsJsonObject("settings").addProperty("lakes", lakes);
			return this;
		}

		/**
		 * Defines if the generator should generate features or not
		 *
		 * @return this
		 */
		public FlatChunkGeneratorTypeBuilder features(boolean features) {
			this.root.getAsJsonObject("settings").addProperty("features", features);
			return this;
		}


		/**
		 * Add a block layer.
		 *
		 * @param layersBuilder builder for each layer
		 * @return this
		 */
		public FlatChunkGeneratorTypeBuilder addLayer(LayersBuilder... layersBuilder) {
			jsonArray(this.root.getAsJsonObject("settings"), "layers", jsonArrayBuilder -> {
				for (LayersBuilder spawnsBuilder : layersBuilder) {
					jsonArrayBuilder.addObject(jsonObjectBuilder -> jsonObjectBuilder
							.add("block", spawnsBuilder.block())
							.add("height", spawnsBuilder.height())
					);
				}
			});
			return this;
		}

		public record LayersBuilder(String block, int height) {
		}
	}
}
