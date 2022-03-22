package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.placementmodifiers;

public class BiomePlacementModifier extends PlacementModifier {
	public BiomePlacementModifier() {
		this.root.addProperty("type", "minecraft:biome");
	}
}
