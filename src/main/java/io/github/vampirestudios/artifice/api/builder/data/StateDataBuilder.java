package io.github.vampirestudios.artifice.api.builder.data;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class StateDataBuilder extends TypedJsonObject {

	private final JsonObject jsonObject = new JsonObject();

	public StateDataBuilder() {
        super(new JsonObject());
	}

	public static StateDataBuilder name(Block block) {
		return name(Registry.BLOCK.getKey(block).toString());
	}

	public static StateDataBuilder name(ResourceLocation id) {
		return name(id.toString());
	}

	/**
	 * Set the id of the block.
	 */
	public static StateDataBuilder name(String id) {
        StateDataBuilder builder = new StateDataBuilder();
        builder.root.addProperty("Name", id);
        return builder;
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
