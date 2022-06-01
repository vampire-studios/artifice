package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;

import java.util.Map;

public class HeightProviderBuilders extends TypedJsonBuilder<JsonObject> {

	public HeightProviderBuilders() {
		super(new JsonObject(), j -> j);
	}

	public HeightProviderBuilders constant(String name, Map.Entry<String, Integer> obj) {
		with(name, JsonObject::new, jsonObject -> this.root.addProperty(obj.getKey(), obj.getValue()));
		return this;
	}

	public HeightProviderBuilders uniform(String name, UniformHeightProviderBuilder processor) {
		with(name, JsonObject::new, jsonObject -> processor.buildTo(this.root));
		return this;
	}

	public HeightProviderBuilders uniform(String name, JsonObject jsonObjectIn) {
		with(name, JsonObject::new, jsonObject -> jsonObject.add("value", jsonObjectIn));
		return this;
	}

	public HeightProviderBuilders biasedToBottom(String name, BiasedToBottomHeightProviderBuilder processor) {
		with(name, JsonObject::new, jsonObject -> processor.buildTo(this.root));
		return this;
	}

	public HeightProviderBuilders veryBiasedToBottom(String name, VeryBiasedToBottomHeightProviderBuilder processor) {
		with(name, JsonObject::new, jsonObject -> processor.buildTo(this.root));
		return this;
	}

	public HeightProviderBuilders trapezoid(String name, TrapezoidHeightProviderBuilder processor) {
		with(name, JsonObject::new, jsonObject -> processor.buildTo(this.root));
		return this;
	}

	public static class UniformHeightProviderBuilder extends TypedJsonBuilder<JsonObject> {

		private final boolean hasFloatValues = false;

		public UniformHeightProviderBuilder() {
			super(new JsonObject(), j -> j);
			this.root.addProperty("type", "minecraft:uniform");
			if (hasFloatValues) this.root.add("value", new JsonObject());
		}

		public UniformHeightProviderBuilder minInclusive(Map.Entry<String, Integer> obj) {
			with("min_inclusive", JsonObject::new, jsonObject -> jsonObject.addProperty(obj.getKey(), obj.getValue()));
			return this;
		}

		public UniformHeightProviderBuilder maxInclusive(Map.Entry<String, Integer> obj) {
			with("max_inclusive", JsonObject::new, jsonObject -> jsonObject.addProperty(obj.getKey(), obj.getValue()));
			return this;
		}

		public static JsonObject minAndMaxInclusive(float minInclusive, float maxExclusive) {
			JsonObject value = new JsonObject();
			value.addProperty("min_inclusive", minInclusive);
			value.addProperty("max_exclusive", maxExclusive);
			return value;
		}

		public static JsonObject minAndMaxInclusive(Map.Entry<String, Integer> minInclusive, Map.Entry<String, Integer> maxExclusive) {
			JsonObject value = new JsonObject();
			with(value, "min_inclusive", JsonObject::new, jsonObject -> jsonObject.addProperty(minInclusive.getKey(), minInclusive.getValue()));
			with(value, "max_exclusive", JsonObject::new, jsonObject -> jsonObject.addProperty(maxExclusive.getKey(), maxExclusive.getValue()));
			return value;
		}

	}

	public static class BiasedToBottomHeightProviderBuilder extends TypedJsonBuilder<JsonObject> {

		public BiasedToBottomHeightProviderBuilder() {
			super(new JsonObject(), j -> j);
			this.root.addProperty("type", "minecraft:biased_to_bottom");
		}

		public BiasedToBottomHeightProviderBuilder absolute(int offset) {
			this.root.addProperty("absolute", offset);
			return this;
		}

		public BiasedToBottomHeightProviderBuilder aboveBottom(int offset) {
			this.root.addProperty("above_bottom", offset);
			return this;
		}

		public BiasedToBottomHeightProviderBuilder belowTop(int offset) {
			this.root.addProperty("below_top", offset);
			return this;
		}

	}

	public static class VeryBiasedToBottomHeightProviderBuilder extends TypedJsonBuilder<JsonObject> {

		public VeryBiasedToBottomHeightProviderBuilder() {
			super(new JsonObject(), j -> j);
			this.root.addProperty("type", "minecraft:very_biased_to_bottom");
		}

		public VeryBiasedToBottomHeightProviderBuilder absolute(int offset) {
			this.root.addProperty("absolute", offset);
			return this;
		}

		public VeryBiasedToBottomHeightProviderBuilder aboveBottom(int offset) {
			this.root.addProperty("above_bottom", offset);
			return this;
		}

		public VeryBiasedToBottomHeightProviderBuilder belowTop(int offset) {
			this.root.addProperty("below_top", offset);
			return this;
		}

	}

	public static class TrapezoidHeightProviderBuilder extends TypedJsonBuilder<JsonObject> {

		public TrapezoidHeightProviderBuilder() {
			super(new JsonObject(), j -> j);
			this.root.addProperty("type", "minecraft:trapezoid");
		}

		public TrapezoidHeightProviderBuilder absolute(int offset) {
			this.root.addProperty("absolute", offset);
			return this;
		}

		public TrapezoidHeightProviderBuilder aboveBottom(int offset) {
			this.root.addProperty("above_bottom", offset);
			return this;
		}

		public TrapezoidHeightProviderBuilder belowTop(int offset) {
			this.root.addProperty("below_top", offset);
			return this;
		}

	}

}
