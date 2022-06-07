package io.github.vampirestudios.artifice.api.builder.data;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;

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
	public StateDataBuilder setProperty(String property, String state) {
		this.jsonObject.addProperty(property, state);
		this.root.add("Properties", this.jsonObject);
		return this;
	}

	public StateDataBuilder setProperty(Property<?> property, Object value) {
		JsonObject obj = new JsonObject();
		if (value instanceof JsonObject) obj.add(property.getName(), (JsonObject) value);
		else if (value instanceof Double) obj.addProperty(property.getName(), (double) value);
		else if (value instanceof Integer) obj.addProperty(property.getName(), (int) value);
		else if (value instanceof Float) obj.addProperty(property.getName(), (float) value);
		else if (value instanceof Boolean) obj.addProperty(property.getName(), (boolean) value);
		else if (value instanceof String) obj.addProperty(property.getName(), (String) value);
		else
			throw new IllegalArgumentException(property.getName() + " must be json object or double or int or float or boolean or string");
		this.root.add("Properties", obj);
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
