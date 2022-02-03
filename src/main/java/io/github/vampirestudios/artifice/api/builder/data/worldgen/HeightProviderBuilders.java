package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class HeightProviderBuilders extends TypedJsonBuilder<JsonObject> {

	public HeightProviderBuilders() {
		super(new JsonObject(), j->j);
	}

	public HeightProviderBuilders constant(String name, Processor<YOffsetBuilder> processor) {
		with(name, JsonObject::new, jsonObject -> processor.process(new YOffsetBuilder()).buildTo(this.root));
		return this;
	}

	public HeightProviderBuilders uniform(String name, Processor<UniformHeightProviderBuilder> processor) {
		with(name, JsonObject::new, jsonObject -> processor.process(new UniformHeightProviderBuilder()).buildTo(this.root));
		return this;
	}

	public HeightProviderBuilders biasedToBottom(String name, Processor<BiasedToBottomHeightProviderBuilder> processor) {
		with(name, JsonObject::new, jsonObject -> processor.process(new BiasedToBottomHeightProviderBuilder()).buildTo(this.root));
		return this;
	}

	public HeightProviderBuilders veryBiasedToBottom(String name, Processor<VeryBiasedToBottomHeightProviderBuilder> processor) {
		with(name, JsonObject::new, jsonObject -> processor.process(new VeryBiasedToBottomHeightProviderBuilder()).buildTo(this.root));
		return this;
	}

	public HeightProviderBuilders trapezoid(String name, Processor<TrapezoidHeightProviderBuilder> processor) {
		with(name, JsonObject::new, jsonObject -> processor.process(new TrapezoidHeightProviderBuilder()).buildTo(this.root));
		return this;
	}

	public static class UniformHeightProviderBuilder extends TypedJsonBuilder<JsonObject> {

		private boolean hasFloatValues = false;

		public UniformHeightProviderBuilder() {
			super(new JsonObject(), j->j);
			this.root.addProperty("type", "minecraft:uniform");
			if(hasFloatValues) this.root.add("value", new JsonObject());
		}

		public UniformHeightProviderBuilder minInclusive(Processor<YOffsetBuilder> processor) {
			with("min_inclusive", JsonObject::new, jsonObject -> processor.process(new YOffsetBuilder()).buildTo(jsonObject));
			return this;
		}

		public UniformHeightProviderBuilder maxInclusive(Processor<YOffsetBuilder> processor) {
			with("max_inclusive", JsonObject::new, jsonObject -> processor.process(new YOffsetBuilder()).buildTo(jsonObject));
			return this;
		}

		public UniformHeightProviderBuilder minAndMaxInclusive(float minInclusive, float maxExclusive) {
			JsonObject value = new JsonObject();
			value.addProperty("min_inclusive", minInclusive);
			value.addProperty("max_exclusive", maxExclusive);
			this.root.add("value", value);
			return this;
		}

	}

	public static class BiasedToBottomHeightProviderBuilder extends TypedJsonBuilder<JsonObject> {

		public BiasedToBottomHeightProviderBuilder() {
			super(new JsonObject(), j->j);
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
			super(new JsonObject(), j->j);
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
			super(new JsonObject(), j->j);
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
