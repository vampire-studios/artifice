package io.github.vampirestudios.artifice.api.builder.data.dimension;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.resource.JsonResource;
import net.minecraft.resources.ResourceLocation;

public class DimensionBuilder extends TypedJsonBuilder<JsonResource<JsonObject>> {
	public DimensionBuilder() {
		super(new JsonObject(), JsonResource::new);
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
	 */
	public <T extends ChunkGeneratorTypeBuilder> DimensionBuilder generator(T generatorBuilderInstance) {
		with("generator", JsonObject::new, generatorBuilderInstance::buildTo);
		return this;
	}

	/**
	 * Make a noise based Chunk Generator.
	 */
	public DimensionBuilder noiseGenerator(ChunkGeneratorTypeBuilder.NoiseChunkGeneratorTypeBuilder generatorBuilder) {
		return this.generator(generatorBuilder);
	}

	/**
	 * Make a flat Chunk Generator.
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
