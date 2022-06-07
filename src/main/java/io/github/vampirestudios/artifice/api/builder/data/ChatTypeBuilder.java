package io.github.vampirestudios.artifice.api.builder.data;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import net.minecraft.resources.ResourceLocation;

public final class ChatTypeBuilder extends TypedJsonObject {
	public ChatTypeBuilder() {
		super(new JsonObject());
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
