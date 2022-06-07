package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.placementmodifiers;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;

public class PlacementModifier extends TypedJsonObject {
	public PlacementModifier() {
		super(new JsonObject());
	}

	public static PlacementModifier biome() {
		PlacementModifier placementModifier = new PlacementModifier();
		placementModifier.add("type", "minecraft:biome");
		return placementModifier;
	}

	public static PlacementModifier inSquare() {
		PlacementModifier placementModifier = new PlacementModifier();
		placementModifier.add("type", "minecraft:in_square");
		return placementModifier;
	}

//	public static PlacementModifier blockPredicate(BlockPredicate blockPredicate) {
//		PlacementModifier placementModifier = new PlacementModifier();
//		placementModifier.add("type", "minecraft:block_predicate_filter");
//		placementModifier.add("predicate", Registry.BLOCK_PREDICATE_TYPES.getKey(blockPredicate.type()).toString());
//		return placementModifier;
//	}

	public static PlacementModifier count() {
		PlacementModifier placementModifier = new PlacementModifier();
		placementModifier.add("type", "minecraft:count");
		return placementModifier;
	}
}
