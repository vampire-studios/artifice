package io.github.vampirestudios.artifice.api.builder.data;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class StateDataBuilder extends TypedJsonBuilder<JsonObject> {

	private final JsonObject jsonObject = new JsonObject();

	public StateDataBuilder() {
		super(new JsonObject(), j -> j);
	}

	public StateDataBuilder name(Block block) {
		return this.name(Registry.BLOCK.getKey(block).toString());
	}

	public StateDataBuilder name(ResourceLocation id) {
		return this.name(id.toString());
	}

	/**
	 * Set the id of the block.
	 */
	public StateDataBuilder name(String id) {
		this.root.addProperty("Name", id);
		return this;
	}

	/**
	 * Set a property to a state.
	 */
	public StateDataBuilder setProperty(Map.Entry<String, String> property) {
		this.jsonObject.addProperty(property.getKey(), property.getValue());
		this.root.add("Properties", this.jsonObject);
		return this;
	}

	@SafeVarargs
	public final StateDataBuilder setProperty(Map.Entry<String, String>... properties) {
		for (Map.Entry<String, String> property : properties) {
			this.jsonObject.addProperty(property.getKey(), property.getValue());
		}
		this.root.add("Properties", this.jsonObject);
		return this;
	}
}
