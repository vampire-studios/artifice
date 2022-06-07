package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class SurfaceRulesBuilder extends TypedJsonObject {

	public SurfaceRulesBuilder() {
		super(new JsonObject());
	}

	public static SurfaceRulesBuilder hole() {
		SurfaceRulesBuilder builder = new SurfaceRulesBuilder();
		builder.add("type", "minecraft:hole");
		return builder;
	}

	public static SurfaceRulesBuilder steep() {
		SurfaceRulesBuilder builder = new SurfaceRulesBuilder();
		builder.add("type", "minecraft:steep");
		return builder;
	}

	public static SurfaceRulesBuilder temperature() {
		SurfaceRulesBuilder builder = new SurfaceRulesBuilder();
		builder.add("type", "minecraft:temperature");
		return builder;
	}

	public static SurfaceRulesBuilder aboveMainSurface() {
		SurfaceRulesBuilder builder = new SurfaceRulesBuilder();
		builder.add("type", "minecraft:above_preliminary_surface");
		return builder;
	}

	//maybe this should be less verbose considering surface rules are literally just a ton of unique entries nested in sequences and conditionals
	public static SurfaceRulesBuilder sequence(SurfaceRulesBuilder... ruleBuilders) {
		SurfaceRulesBuilder builder = new SurfaceRulesBuilder();
		builder.add("sequence", arrayOf(ruleBuilders))
				.add("type", "minecraft:sequence");
		return builder;
	}

	public static SurfaceRulesBuilder condition(SurfaceRulesBuilder ifTrue, SurfaceRulesBuilder thenRun) {
		SurfaceRulesBuilder builder = new SurfaceRulesBuilder();
		builder.add("if_true", ifTrue.build())
				.add("then_run", thenRun.build())
				.add("type", "minecraft:condition");
		return builder;
	}

	public static SurfaceRulesBuilder bandlands() {
		SurfaceRulesBuilder builder = new SurfaceRulesBuilder();
		builder.add("type", "minecraft:bandlands");
		return builder;
	}

	public static SurfaceRulesBuilder block(StateDataBuilder blockStateBuilderProcessor) {
		SurfaceRulesBuilder builder = new SurfaceRulesBuilder();
		builder.join("result_state", blockStateBuilderProcessor.build());
		builder.add("type", "minecraft:block");
		return builder;
	}

	public static SurfaceRulesBuilder verticalGradient(String randomName, YOffsetBuilder trueAtAndBelow, YOffsetBuilder falseAtAndAbove) {
		SurfaceRulesBuilder builder = new SurfaceRulesBuilder();
		builder.add("random_name", randomName)
				.add("true_at_and_below", trueAtAndBelow.build())
				.add("false_at_and_above", falseAtAndAbove.build())
				.add("type", "minecraft:vertical_gradient");
		return builder;
	}

	public static SurfaceRulesBuilder not(SurfaceRulesBuilder rule) {
		SurfaceRulesBuilder builder = new SurfaceRulesBuilder();
		builder.add("invert", rule.build());
		builder.add("type", "minecraft:not");
		return builder;
	}

	public static SurfaceRulesBuilder biome(ResourceKey<Biome>... biomes) {
		List<ResourceLocation> values = new ArrayList<>();
		for (ResourceKey<Biome> biome : biomes) {
			values.add(biome.location());
		}
		return biome(values.toArray(new ResourceLocation[0]));
	}

	public static SurfaceRulesBuilder biome(ResourceLocation... biomes) {
		List<String> values = new ArrayList<>();
		for (ResourceLocation biome : biomes) {
			values.add(biome.toString());
		}
		return biome(values.toArray(new String[0]));
	}

	public static SurfaceRulesBuilder biome(String... biomes) {
		SurfaceRulesBuilder builder = new SurfaceRulesBuilder();
		builder.add("biome_is", arrayOf(biomes));
		builder.add("type", "minecraft:biome");
		return builder;
	}

	public static SurfaceRulesBuilder yAbove(YOffsetBuilder yPos, int surfaceDepthMultiplier, boolean addStoneDepth) {
		SurfaceRulesBuilder builder = new SurfaceRulesBuilder();
		builder.add("anchor", yPos.build())
				.add("surface_depth_multiplier", surfaceDepthMultiplier)
				.add("add_stone_depth", addStoneDepth)
				.add("type", "minecraft:y_above");
		return builder;
	}

	public static SurfaceRulesBuilder water(int offset, int surfaceDepthMultiplier, boolean addStoneDepth) {
		SurfaceRulesBuilder builder = new SurfaceRulesBuilder();
		builder.add("offset", offset)
				.add("surface_depth_multiplier", surfaceDepthMultiplier)
				.add("add_stone_depth", addStoneDepth)
				.add("type", "minecraft:water");
		return builder;
	}

	public static SurfaceRulesBuilder noiseThreshold(String noiseId, double min, double max) {
		SurfaceRulesBuilder builder = new SurfaceRulesBuilder();
		builder.add("noise", noiseId)
				.add("min_threshold", min)
				.add("max_threshold", max)
				.add("type", "minecraft:noise_threshold");
		return builder;
	}

	/*
	 * @Deprecated in 1.18.2
	 */
	public SurfaceRulesBuilder stoneDepth(String surfaceType, int offset, boolean addSurfaceDepth, boolean addSurfaceSecondaryDepth) {
		root.addProperty("surface_type", surfaceType);
		root.addProperty("offset", offset);
		root.addProperty("add_Surface_depth", addSurfaceDepth);
		root.addProperty("add_surface_secondary_depth", addSurfaceSecondaryDepth);
		root.addProperty("type", "minecraft:stone_depth");
		return this;
	}

	public SurfaceRulesBuilder stoneDepth(String surfaceType, int offset, boolean addSurfaceDepth, int secondaryDepthRange) {
		root.addProperty("surface_type", surfaceType);
		root.addProperty("offset", offset);
		root.addProperty("add_Surface_depth", addSurfaceDepth);
		root.addProperty("secondary_depth_range", secondaryDepthRange);
		root.addProperty("type", "minecraft:stone_depth");
		return this;
	}

}
