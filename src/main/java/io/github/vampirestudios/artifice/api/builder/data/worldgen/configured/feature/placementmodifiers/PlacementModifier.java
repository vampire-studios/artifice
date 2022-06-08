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

}