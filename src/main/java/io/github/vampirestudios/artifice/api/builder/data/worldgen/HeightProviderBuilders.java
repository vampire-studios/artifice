package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;

public class HeightProviderBuilders extends TypedJsonObject {

	public HeightProviderBuilders() {
		super(new JsonObject());
	}

	public HeightProviderBuilders constant(String name, YOffsetBuilder obj) {
		join(name, obj.getData());
		return this;
	}

	public static HeightProviderBuilders uniform(YOffsetBuilder minInclusive, YOffsetBuilder maxInclusive) {
		HeightProviderBuilders builder = new HeightProviderBuilders();

		builder.add("type", "minecraft:uniform")
				.add("min_inclusive", minInclusive)
				.add("max_inclusive", maxInclusive);

		return builder;
	}

	public HeightProviderBuilders biasedToBottom(YOffsetBuilder minInclusive, YOffsetBuilder maxInclusive, int inner) {
		HeightProviderBuilders builder = new HeightProviderBuilders();

		builder.add("type", "minecraft:biased_to_bottom")
				.add("min_inclusive", minInclusive)
				.add("max_inclusive", maxInclusive)
				.add("inner",inner);

		return builder;
	}

	public HeightProviderBuilders veryBiasedToBottom(YOffsetBuilder minInclusive, YOffsetBuilder maxInclusive, int inner) {
		HeightProviderBuilders builder = new HeightProviderBuilders();

		builder.add("type", "minecraft:very_biased_to_bottom")
				.add("min_inclusive", minInclusive)
				.add("max_inclusive", maxInclusive)
				.add("inner",inner);

		return builder;
	}

	public HeightProviderBuilders trapezoid(YOffsetBuilder minInclusive, YOffsetBuilder maxInclusive, int plateau) {
		HeightProviderBuilders builder = new HeightProviderBuilders();

		builder.add("type", "minecraft:trapezoid")
				.add("min_inclusive", minInclusive)
				.add("max_inclusive", maxInclusive)
				.add("plateau",plateau);

		return builder;
	}

	/*public HeightProviderBuilders weightedList(String name, TrapezoidHeightProviderBuilder processor) {
		HeightProviderBuilders builder = new HeightProviderBuilders();

		TypedJsonObject value = new TypedJsonObject()
				.add("min_inclusive", minInclusive)
				.add("max_inclusive", maxInclusive)
				.add("inner",inner);
		TypedJsonObject uniform = new TypedJsonObject()
				.add("type", "minecraft:uniform")
				.add("value", value);

		builder.add(name, uniform.getData());

		return builder;
	} */
}
