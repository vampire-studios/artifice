package io.github.vampirestudios.artifice.api.builder.data.dimension;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.structure.SpawnsBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class ChunkGeneratorTypeBuilder extends TypedJsonObject {

    protected ChunkGeneratorTypeBuilder() {
        super(new JsonObject());
    }

    /**
     * Set the type (ID) of the Chunk Generator.
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
     * @param biomeSourceBuilder
     * @param <T>
     * @return
     */
    public <T extends BiomeSourceBuilder> ChunkGeneratorTypeBuilder biomeSource(T biomeSourceBuilder) {
        join("biome_source", biomeSourceBuilder.getData());
        return this;
    }


    public static class NoiseChunkGeneratorTypeBuilder extends ChunkGeneratorTypeBuilder {
        public NoiseChunkGeneratorTypeBuilder() {
            super();
            this.type("minecraft:noise");
        }

        /**
         * Set a seed specially for this dimension.
         * @param seed
         * @return
         */
        public NoiseChunkGeneratorTypeBuilder seed(int seed) {
            this.root.addProperty("seed", seed);
            return this;
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
         * @param noiseSettingsID the identifier
         * @return this
         */
        public NoiseChunkGeneratorTypeBuilder noiseSettings(String noiseSettingsID) {
            this.root.addProperty("settings", noiseSettingsID);
            return this;
        }

        /**
         * Build a vanilla layered biome source.
         * @param biomeSourceBuilder
         * @return this
         */
        public NoiseChunkGeneratorTypeBuilder vanillaLayeredBiomeSource(BiomeSourceBuilder.VanillaLayeredBiomeSourceBuilder biomeSourceBuilder) {
            biomeSource(biomeSourceBuilder);
            return this;
        }

        /**
         * Build a multi-noise biome source.
         * @param biomeSourceBuilder
         * @return this
         */
        public NoiseChunkGeneratorTypeBuilder multiNoiseBiomeSource(BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder biomeSourceBuilder) {
            biomeSource(biomeSourceBuilder);
            return this;
        }

        /**
         * Build a checkerboard biome source.
         * @param biomeSourceBuilder
         * @return this
         */
        public NoiseChunkGeneratorTypeBuilder checkerboardBiomeSource(BiomeSourceBuilder.CheckerboardBiomeSourceBuilder biomeSourceBuilder) {
            biomeSource(biomeSourceBuilder);
            return this;
        }

        /**
         * Build a fixed biome source.
         * @param biomeSourceBuilder
         * @return this
         */
        public NoiseChunkGeneratorTypeBuilder fixedBiomeSource(BiomeSourceBuilder.FixedBiomeSourceBuilder biomeSourceBuilder) {
            biomeSource(biomeSourceBuilder);
            return this;
        }

        /**
         * Build a simple biome source.
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
         * @param biomeId the biome id.
         * @return this
         */
        public FlatChunkGeneratorTypeBuilder biome(String biomeId) {
            this.root.getAsJsonObject("settings").addProperty("biome", biomeId);
            return this;
        }

        /**
         * Defines if the generator should generate lakes  or not
         * @return this
         */
        public FlatChunkGeneratorTypeBuilder lakes(boolean lakes) {
            this.root.getAsJsonObject("settings").addProperty("lakes", lakes);
            return this;
        }

        /**
         * Defines if the generator should generate features or not
         * @return this
         */
        public FlatChunkGeneratorTypeBuilder features(boolean features) {
            this.root.getAsJsonObject("settings").addProperty("features", features);
            return this;
        }


        /**
         * Add a block layer.
         * @param layersBuilder builder for each layer
         * @return this
         */
        public FlatChunkGeneratorTypeBuilder addLayer(LayersBuilder... layersBuilder) {

            JsonArray aaa = new JsonArray();
            for (LayersBuilder spawnsBuilder : layersBuilder) {
                aaa.add(new TypedJsonObject()
                        .add("block", spawnsBuilder.block())
                        .add("height", spawnsBuilder.height()).getData());
            }
            this.join(getObj("settings"),"layers", aaa);
            return this;
        }

        public record LayersBuilder(String block, int height){}
    }
}
