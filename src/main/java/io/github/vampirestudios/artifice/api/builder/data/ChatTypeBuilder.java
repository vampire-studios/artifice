package io.github.vampirestudios.artifice.api.builder.data;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.resource.JsonResource;
import net.minecraft.resources.ResourceLocation;

public final class ChatTypeBuilder extends TypedJsonBuilder<JsonResource<JsonObject>> {
	public ChatTypeBuilder() {
		super(new JsonObject(), JsonResource::new);
	}

	/**
	 * @return this
	 */
	public ChatTypeBuilder chat(ResourceLocation id) {
		root.addProperty("type", id.toString());
		return this;
	}

	/**
	 * @return this
	 */
	public ChatTypeBuilder overlay(ResourceLocation id) {
		root.addProperty("type", id.toString());
		return this;
	}

	/**
	 * @return this
	 */
	public ChatTypeBuilder narration(ResourceLocation id) {
		root.addProperty("type", id.toString());
		return this;
	}

}
