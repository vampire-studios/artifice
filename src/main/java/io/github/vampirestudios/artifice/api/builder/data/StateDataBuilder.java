package io.github.vampirestudios.artifice.api.builder.data;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;

import java.util.Map;

public class StateDataBuilder extends TypedJsonObject {

	private final JsonObject jsonObject = new JsonObject();

	public StateDataBuilder() {
		super(new JsonObject());
	}

	public static StateDataBuilder name(Block block) {
		return name(Registry.BLOCK.getKey(block).toString());
	}

	public static StateDataBuilder name(Fluid block) {
		return name(Registry.FLUID.getKey(block).toString());
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
		switch (value) {

			case JsonObject object -> obj.add(property.getName(), object);
			case Double aDouble -> obj.addProperty(property.getName(), aDouble);
			case Integer integer -> obj.addProperty(property.getName(), integer);
			case Float aFloat -> obj.addProperty(property.getName(), aFloat);
			case Boolean aBoolean -> obj.addProperty(property.getName(), aBoolean);
			case String s -> obj.addProperty(property.getName(), s);
			case null, default ->
					throw new IllegalArgumentException(property.getName() + " must be json object or double or int or float or boolean or string");
		}
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
