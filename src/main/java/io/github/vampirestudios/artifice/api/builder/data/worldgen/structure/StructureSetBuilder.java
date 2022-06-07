package io.github.vampirestudios.artifice.api.builder.data.worldgen.structure;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

public class StructureSetBuilder extends TypedJsonObject {
	public StructureSetBuilder() {
		super(new JsonObject());
	}

	public StructureSetBuilder structures(StructureEntry[] structures) {
		JsonArray array = new JsonArray();
		for (StructureEntry structure : structures) {
			JsonObject structureEntry = new JsonObject();
			structureEntry.addProperty("structure", structure.structure.toString());
			structureEntry.addProperty("weight", structure.weight);
			array.add(structureEntry);
		}
		this.add("structures", arrayOf(array));
		return this;
	}

	public StructureSetBuilder concentricRingsType(Codec<StructureSet> codec, int distance, int count, int spread) {
		JsonObject type = new JsonObject();
		type.addProperty("distance", distance);
		type.addProperty("count", distance);
		type.addProperty("spread", distance);
		this.root.add("type", type);
		return this;
	}

	public StructureSetBuilder randomSpreadType(RandomSpreadType spreadType, int locateOffset, int distance, int count, int spread) {
		JsonObject type = new JsonObject();
		type.addProperty("spread_type", spreadType.getSerializedName());
		type.addProperty("locate_offset", locateOffset);
		type.addProperty("distance", distance);
		type.addProperty("count", count);
		type.addProperty("spread", spread);
		this.root.add("type", type);
		return this;
	}

	public StructureSetBuilder randomSpreadType(RandomSpreadType spreadType, int distance, int count, int spread) {
		JsonObject type = new JsonObject();
		type.addProperty("distance", distance);
		type.addProperty("count", count);
		type.addProperty("spread", spread);
		this.root.add("type", type);
		return this;
	}

	public StructureSetBuilder randomSpreadType(int locateOffset, int distance, int count, int spread) {
		JsonObject type = new JsonObject();
		type.addProperty("locate_offset", locateOffset);
		type.addProperty("distance", distance);
		type.addProperty("count", count);
		type.addProperty("spread", spread);
		this.root.add("type", type);
		return this;
	}

	public StructureSetBuilder randomSpreadType(int distance, int count, int spread) {
		JsonObject type = new JsonObject();
		type.addProperty("distance", distance);
		type.addProperty("count", count);
		type.addProperty("spread", spread);
		this.root.add("type", type);
		return this;
	}

	public record StructureEntry(ResourceLocation structure, int weight) {
	}

}