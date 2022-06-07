package io.github.vampirestudios.artifice.api.builder.data.dimension;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;

public class BiomeSourceBuilder extends TypedJsonObject {

	public BiomeSourceBuilder() {
		super(new JsonObject());
	}

	/**
	 * Set the type of the biome source.
	 */
	public <T extends BiomeSourceBuilder> T type(String type) {
		this.root.addProperty("type", type);
		return (T) this;
	}

	public static MultiNoiseBiomeSourceBuilder multiNoise() {
		return new MultiNoiseBiomeSourceBuilder();
	}

	public static MultiNoiseBiomeSourceBuilder.BiomeBuilder noiseBiome(String id, MultiNoiseBiomeSourceBuilder.BiomeParametersBuilder biomeSettingsBuilder) {
		return new MultiNoiseBiomeSourceBuilder.BiomeBuilder().biome(id).parameters(biomeSettingsBuilder);
	}

	public static MultiNoiseBiomeSourceBuilder.BiomeParametersBuilder noiseBiomeParameters() {
		return new MultiNoiseBiomeSourceBuilder.BiomeParametersBuilder();
	}

	public static class MultiNoiseBiomeSourceBuilder extends BiomeSourceBuilder {
		public MultiNoiseBiomeSourceBuilder() {
			super();
			this.type("minecraft:multi_noise");
		}

		/**
		 * Set the preset.
		 */
		public MultiNoiseBiomeSourceBuilder preset(String preset) {
			this.root.addProperty("preset", preset);
			return this;
		}

		/**
		 * Add biome.
		 */
		public MultiNoiseBiomeSourceBuilder biomes(BiomeBuilder... biomeBuilder) {
			join("biomes", arrayOf(biomeBuilder));
			return this;
		}

		public static class BiomeBuilder extends TypedJsonObject {
			public BiomeBuilder() {
				super(new JsonObject());
			}

			public BiomeBuilder biome(String id) {
				this.add("biome", id);
				return this;
			}

			/**
			 * Build biome parameters.
			 */
			public BiomeBuilder parameters(BiomeParametersBuilder biomeSettingsBuilder) {
				join("parameters", biomeSettingsBuilder.build());
				return this;
			}
		}

		public static class BiomeParametersBuilder extends TypedJsonObject {
			public BiomeParametersBuilder() {
				super(new JsonObject());
			}

			public BiomeParametersBuilder temperature(float temperature) {
				if (temperature > 2.0F)
					throw new IllegalArgumentException("temperature can't be higher than 2.0F! Found " + temperature);
				if (temperature < -2.0F)
					throw new IllegalArgumentException("temperature can't be smaller than 2.0F! Found " + temperature);
				this.root.addProperty("temperature", temperature);
				return this;
			}

			public BiomeParametersBuilder temperature(float min, float max) {
				if (max > 2.0F)
					throw new IllegalArgumentException("temperature can't be higher than 2.0F! Found " + max);
				if (min < -2.0F)
					throw new IllegalArgumentException("temperature can't be smaller than 2.0F! Found " + min);
				this.add("temperature", arrayOf(min, max));
				return this;
			}

			public BiomeParametersBuilder humidity(float humidity) {
				if (humidity > 2.0F)
					throw new IllegalArgumentException("humidity can't be higher than 2.0F! Found " + humidity);
				if (humidity < -2.0F)
					throw new IllegalArgumentException("humidity can't be smaller than 2.0F! Found " + humidity);
				this.root.addProperty("humidity", humidity);
				return this;
			}

			public BiomeParametersBuilder humidity(float min, float max) {
				if (max > 2.0F) throw new IllegalArgumentException("humidity can't be higher than 2.0F! Found " + max);
				if (min < -2.0F)
					throw new IllegalArgumentException("humidity can't be smaller than 2.0F! Found " + min);
				this.add("humidity", arrayOf(min, max));
				return this;
			}

			public BiomeParametersBuilder continentalness(float continentalness) {
				if (continentalness > 2.0F)
					throw new IllegalArgumentException("continentalness can't be higher than 2.0F! Found " + continentalness);
				if (continentalness < -2.0F)
					throw new IllegalArgumentException("continentalness can't be smaller than 2.0F! Found " + continentalness);
				this.root.addProperty("continentalness", continentalness);
				return this;
			}

			public BiomeParametersBuilder continentalness(float min, float max) {
				if (max > 2.0F)
					throw new IllegalArgumentException("continentalness can't be higher than 2.0F! Found " + max);
				if (min < -2.0F)
					throw new IllegalArgumentException("continentalness can't be smaller than 2.0F! Found " + min);
				this.add("continentalness", arrayOf(min, max));
				return this;
			}

			public BiomeParametersBuilder erosion(float erosion) {
				if (erosion > 2.0F)
					throw new IllegalArgumentException("erosion can't be higher than 2.0F! Found " + erosion);
				if (erosion < -2.0F)
					throw new IllegalArgumentException("erosion can't be smaller than 2.0F! Found " + erosion);
				this.root.addProperty("erosion", erosion);
				return this;
			}

			public BiomeParametersBuilder erosion(float min, float max) {
				if (max > 2.0F) throw new IllegalArgumentException("erosion can't be higher than 2.0F! Found " + max);
				if (min < -2.0F) throw new IllegalArgumentException("erosion can't be smaller than 2.0F! Found " + min);
				this.add("erosion", arrayOf(min, max));
				return this;
			}

			public BiomeParametersBuilder weirdness(float weirdness) {
				if (weirdness > 2.0F)
					throw new IllegalArgumentException("weirdness can't be higher than 2.0F! Found " + weirdness);
				if (weirdness < -2.0F)
					throw new IllegalArgumentException("weirdness can't be smaller than 2.0F! Found " + weirdness);
				this.root.addProperty("weirdness", weirdness);
				return this;
			}

			public BiomeParametersBuilder weirdness(float min, float max) {
				if (max > 2.0F) throw new IllegalArgumentException("weirdness can't be higher than 2.0F! Found " + max);
				if (min < -2.0F)
					throw new IllegalArgumentException("weirdness can't be smaller than 2.0F! Found " + min);
				this.add("weirdness", arrayOf(min, max));
				return this;
			}

			public BiomeParametersBuilder depth(float depth) {
				if (depth > 1.0F) throw new IllegalArgumentException("depth can't be higher than 1.0F! Found " + depth);
				if (depth < 0.0F)
					throw new IllegalArgumentException("depth can't be smaller than 0.0F! Found " + depth);
				this.root.addProperty("depth", depth);
				return this;
			}

			public BiomeParametersBuilder depth(float min, float max) {
				if (max > 2.0F) throw new IllegalArgumentException("depth can't be higher than 2.0F! Found " + max);
				if (min < -2.0F) throw new IllegalArgumentException("depth can't be smaller than 2.0F! Found " + min);
				this.add("depth", arrayOf(min, max));
				return this;
			}

			public BiomeParametersBuilder offset(float offset) {
				if (offset > 1.0F)
					throw new IllegalArgumentException("offset can't be higher than 1.0F! Found " + offset);
				if (offset < 0.0F)
					throw new IllegalArgumentException("offset can't be smaller than 0.0F! Found " + offset);
				this.root.addProperty("offset", offset);
				return this;
			}
		}
	}

	public static CheckerboardBiomeSourceBuilder checkerBoard(int scale, String... biomeID) {
		return new CheckerboardBiomeSourceBuilder().scale(scale).addBiome(biomeID);
	}

	public static class CheckerboardBiomeSourceBuilder extends BiomeSourceBuilder {

		public CheckerboardBiomeSourceBuilder() {
			super();
			this.type("minecraft:checkerboard");
			this.root.add("biomes", new JsonArray());
		}

		public CheckerboardBiomeSourceBuilder scale(int scale) {
			if (scale > 62) throw new IllegalArgumentException("Scale can't be higher than 62! Found " + scale);
			if (scale < 0) throw new IllegalArgumentException("Scale can't be smaller than 0! Found " + scale);
			this.root.addProperty("scale", scale);
			return this;
		}

		public CheckerboardBiomeSourceBuilder addBiome(String... biomeId) {
			this.join("biomes", arrayOf(biomeId));
			return this;
		}
	}

	public static FixedBiomeSourceBuilder fixed(String biomeID) {
		return new FixedBiomeSourceBuilder().biome(biomeID);
	}

	public static class FixedBiomeSourceBuilder extends BiomeSourceBuilder {
		public FixedBiomeSourceBuilder() {
			super();
			this.type("minecraft:fixed");
		}

		public FixedBiomeSourceBuilder biome(String biomeId) {
			this.root.addProperty("biome", biomeId);
			return this;
		}
	}
}
