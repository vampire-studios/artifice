package io.github.vampirestudios.artifice.api.builder.data.dimension;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.util.Processor;

public class BiomeSourceBuilder extends TypedJsonObject {

    public BiomeSourceBuilder() {
        super(new JsonObject());
    }

    /**
     * Set the seed of the biome source.
     * @param seed
     * @param <T>
     * @return
     */
    public <T extends BiomeSourceBuilder> T seed(int seed) {
        this.root.addProperty("seed", seed);
        return (T)this;
    }

    /**
     * Set the type of the biome source.
     * @param type
     * @param <T>
     * @return
     */
    public <T extends BiomeSourceBuilder> T type(String type) {
        this.root.addProperty("type", type);
        return (T) this;
    }


    public static class VanillaLayeredBiomeSourceBuilder extends BiomeSourceBuilder {
        public VanillaLayeredBiomeSourceBuilder() {
            super();
            this.type("minecraft:vanilla_layered");
        }

        /**
         * @param largeBiomes
         * @return
         */
        public VanillaLayeredBiomeSourceBuilder largeBiomes(boolean largeBiomes) {
            this.root.addProperty("large_biomes", largeBiomes);
            return this;
        }

        /**
         * @param legacyBiomeInitLayer
         * @return
         */
        public VanillaLayeredBiomeSourceBuilder legacyBiomeInitLayer(boolean legacyBiomeInitLayer) {
            this.root.addProperty("legacy_biome_init_layer", legacyBiomeInitLayer);
            return this;
        }
    }

    public static class MultiNoiseBiomeSourceBuilder extends BiomeSourceBuilder {
        public MultiNoiseBiomeSourceBuilder() {
            super();
            this.type("minecraft:multi_noise");
        }

        /**
         * Set the preset.
         * @param preset
         * @return
         */
        public MultiNoiseBiomeSourceBuilder preset(String preset) {
            this.root.addProperty("preset", preset);
            return this;
        }

        /**
         * Add biome.
         * @param biomeBuilder
         * @return
         */
        public MultiNoiseBiomeSourceBuilder addBiome(BiomeBuilder biomeBuilder) {
            join("biomes", arrayOf(biomeBuilder));
            return this;
        }

        public static class BiomeBuilder extends TypedJsonObject {
            protected BiomeBuilder() {
                super(new JsonObject());
            }

            /**
             * Set the biome ID.
             * @param id
             * @return
             */
            public BiomeBuilder biome(String id) {
                this.root.addProperty("biome", id);
                return this;
            }

            /**
             * Build biome parameters.
             * @param biomeSettingsBuilder
             * @return
             */
            public BiomeBuilder parameters(BiomeParametersBuilder biomeSettingsBuilder) {
                join("parameters", biomeSettingsBuilder.getData());
                return this;
            }
        }

        public static class BiomeParametersBuilder extends TypedJsonObject {
            protected BiomeParametersBuilder() {
                super(new JsonObject());
            }

            /**
             * @param altitude
             * @return
             */
            public BiomeParametersBuilder altitude(float altitude) {
                if (altitude > 2.0F) throw new IllegalArgumentException("altitude can't be higher than 2.0F! Found " + altitude);
                if (altitude < -2.0F) throw new IllegalArgumentException("altitude can't be smaller than 2.0F! Found " + altitude);
                this.root.addProperty("altitude", altitude);
                return this;
            }

            /**
             * @param weirdness
             * @return
             */
            public BiomeParametersBuilder weirdness(float weirdness) {
                if (weirdness > 2.0F) throw new IllegalArgumentException("weirdness can't be higher than 2.0F! Found " + weirdness);
                if (weirdness < -2.0F) throw new IllegalArgumentException("weirdness can't be smaller than 2.0F! Found " + weirdness);
                this.root.addProperty("weirdness", weirdness);
                return this;
            }

            /**
             * @param offset
             * @return
             */
            public BiomeParametersBuilder offset(float offset) {
                if (offset > 1.0F) throw new IllegalArgumentException("offset can't be higher than 1.0F! Found " + offset);
                if (offset < 0.0F) throw new IllegalArgumentException("offset can't be smaller than 0.0F! Found " + offset);
                this.root.addProperty("offset", offset);
                return this;
            }

            /**
             * @param temperature
             * @return
             */
            public BiomeParametersBuilder temperature(float temperature) {
                if (temperature > 2.0F) throw new IllegalArgumentException("temperature can't be higher than 2.0F! Found " + temperature);
                if (temperature < -2.0F) throw new IllegalArgumentException("temperature can't be smaller than 2.0F! Found " + temperature);
                this.root.addProperty("temperature", temperature);
                return this;
            }

            /**
             * @param humidity
             * @return
             */
            public BiomeParametersBuilder humidity(float humidity) {
                if (humidity > 2.0F) throw new IllegalArgumentException("humidity can't be higher than 2.0F! Found " + humidity);
                if (humidity < -2.0F) throw new IllegalArgumentException("humidity can't be smaller than 2.0F! Found " + humidity);
                this.root.addProperty("humidity", humidity);
                return this;
            }
        }

        /**
         * @param noiseSettings
         * @return this
         */
        public MultiNoiseBiomeSourceBuilder altitudeNoise(NoiseSettings noiseSettings) {
            join("altitude_noise", noiseSettings.getData());
            return this;
        }

        /**
         * @param noiseSettings
         * @return this
         */
        public MultiNoiseBiomeSourceBuilder weirdnessNoise(NoiseSettings noiseSettings) {
            join("weirdness_noise", noiseSettings.getData());
            return this;
        }

        /**
         * @param noiseSettings
         * @return this
         */
        public MultiNoiseBiomeSourceBuilder temperatureNoise(NoiseSettings noiseSettings) {
            join("temperature_noise", noiseSettings.getData());
            return this;
        }

        /**
         * @param noiseSettings
         * @return this
         */
        public MultiNoiseBiomeSourceBuilder humidityNoise(NoiseSettings noiseSettings) {
            join("humidity_noise", noiseSettings.getData());
            return this;
        }

        public static class NoiseSettings extends TypedJsonObject {
            protected NoiseSettings() {
                super(new JsonObject());
            }

            /**
             * Changes how much detail the noise of the respective value has
             * @param octave how much detail the noise of the respective value has
             * @return this
             */
            public NoiseSettings firstOctave(int octave) {
                this.root.addProperty("firstOctave", octave);
                return this;
            }

            /**
             * @param amplitudes the amplitudes you want
             * @return this
             */
            public NoiseSettings amplitudes(float... amplitudes) {
                this.join("amplitudes", arrayOf(amplitudes));
                return this;
            }
        }

        public static class AmplitudesBuilder extends TypedJsonObject {
            protected AmplitudesBuilder() {
                super(new JsonObject());
            }

            /**
             * @param amplitude idk
             * @return
             */
            public AmplitudesBuilder amplitude(float amplitude) {
                this.root.addProperty("altitude", amplitude);
                return this;
            }
        }

    }

    public static class CheckerboardBiomeSourceBuilder extends BiomeSourceBuilder {

        public CheckerboardBiomeSourceBuilder() {
            super();
            this.type("minecraft:checkerboard");
            this.root.add("biomes", new JsonArray());
        }

        /**
         * @param scale
         * @return
         */
        public CheckerboardBiomeSourceBuilder scale(int scale) {
            if (scale > 62) throw new IllegalArgumentException("Scale can't be higher than 62! Found " + scale);
            if (scale < 0) throw new IllegalArgumentException("Scale can't be smaller than 0! Found " + scale);
            this.root.addProperty("scale", scale);
            return this;
        }

        /**
         * Add biome.
         * @param biomeId
         * @return
         */
        public CheckerboardBiomeSourceBuilder addBiome(String biomeId) {
            this.root.getAsJsonArray("biomes").add(biomeId);
            return this;
        }
    }

    public static class FixedBiomeSourceBuilder extends BiomeSourceBuilder {
        public FixedBiomeSourceBuilder() {
            super();
            this.type("minecraft:fixed");
        }

        /**
         * Set biome ID.
         * @param biomeId
         * @return
         */
        public FixedBiomeSourceBuilder biome(String biomeId) {
            this.root.addProperty("biome", biomeId);
            return this;
        }
    }
}
