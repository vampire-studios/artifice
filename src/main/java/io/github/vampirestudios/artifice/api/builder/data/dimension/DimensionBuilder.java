package io.github.vampirestudios.artifice.api.builder.data.dimension;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import net.minecraft.resources.ResourceLocation;

public class DimensionBuilder extends TypedJsonObject {
    public DimensionBuilder() {
        super(new JsonObject());
    }

	/**
	 * Set the dimension type.
	 */
	public DimensionBuilder dimensionType(ResourceLocation identifier) {
		this.root.addProperty("type", identifier.toString());
		return this;
	}

    /**
     * Make a Chunk Generator.
     * @param generatorBuilder
     * @param <T> A class extending ChunkGeneratorTypeBuilder.
     * @return
     */
    public <T extends ChunkGeneratorTypeBuilder> DimensionBuilder generator(T generatorBuilder) {
        join("generator", generatorBuilder.build() );
        return this;
    }

    /**
     * Make a noise based Chunk Generator.
     * @param generatorBuilder
     * @return
     */
    public DimensionBuilder noiseGenerator(ChunkGeneratorTypeBuilder.NoiseChunkGeneratorTypeBuilder generatorBuilder) {
        return this.generator(generatorBuilder);
    }

    /**
     * Make a flat Chunk Generator.
     * @param generatorBuilder
     * @return
     */
    public DimensionBuilder flatGenerator(ChunkGeneratorTypeBuilder.FlatChunkGeneratorTypeBuilder generatorBuilder) {
        return this.generator(generatorBuilder);
    }

	/**
	 * Use with a Chunk Generator which doesn't need any configuration. Example: Debug Mode Generator.
	 *
	 * @param generatorId The ID of the chunk generator type.
	 * @return this
	 */
	public DimensionBuilder simpleGenerator(String generatorId) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("type", generatorId);
		this.root.add("generator", jsonObject);
		return this;
	}
}
