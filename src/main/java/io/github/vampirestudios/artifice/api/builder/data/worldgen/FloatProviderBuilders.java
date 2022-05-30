package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;

public class FloatProviderBuilders extends TypedJsonObject {

	public FloatProviderBuilders() {
		super(new JsonObject());
	}

	public FloatProviderBuilders constant(String name, float cons) {
		add(name, cons);
		return this;
	}

	public static FloatProviderBuilders uniform(float minInclusive, float maxExclusive) {
		FloatProviderBuilders builder = new FloatProviderBuilders();

		TypedJsonObject value = new TypedJsonObject()
			.add("min_inclusive", minInclusive)
			.add("max_exclusive", maxExclusive);
		builder.add("type", "minecraft:uniform")
			.add("value", value.getData());

		return builder;
	}

	public FloatProviderBuilders clampedNormal(float mean, float deviation, float min, float max) {
		FloatProviderBuilders builder = new FloatProviderBuilders();

		TypedJsonObject value = new TypedJsonObject()
				.add("mean", mean)
				.add("deviation", deviation)
				.add("min", min)
				.add("max", max);
		builder.add("type", "minecraft:clamped_normal")
				.add("value", value.getData());

		return builder;
	}

	public FloatProviderBuilders trapezoid(float min, float max, float plateau) {
		FloatProviderBuilders builder = new FloatProviderBuilders();

		TypedJsonObject value = new TypedJsonObject()
				.add("min", min)
				.add("max", max)
				.add("plateau", plateau);
		builder.add("type", "minecraft:trapezoid")
				.add("value", value.getData());

		return builder;
	}


}
