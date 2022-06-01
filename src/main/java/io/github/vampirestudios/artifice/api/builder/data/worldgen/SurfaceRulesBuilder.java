package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SurfaceRulesBuilder extends TypedJsonBuilder<JsonObject> {

	public SurfaceRulesBuilder() {
		super(new JsonObject(), j -> j);
	}

	public static Map.Entry<String, String> hole() {
		return Map.entry("type", "minecraft:hole");
	}

	public static Map.Entry<String, String> steep() {
		return Map.entry("type", "minecraft:steep");
	}

	public static Map.Entry<String, String> temperature() {
		return Map.entry("type", "minecraft:temperature");
	}

	public static Map.Entry<String, String> aboveMainSurface() {
		return Map.entry("type", "minecraft:above_preliminary_surface");
	}

	//maybe this should be less verbose considering surface rules are literally just a ton of unique entries nested in sequences and conditionals
	public SurfaceRulesBuilder sequence(SurfaceRulesBuilder... ruleBuilders) {
		jsonArray("sequence", jsonArrayBuilder -> {
			for (SurfaceRulesBuilder rule : ruleBuilders) {
				jsonArrayBuilder.add(rule.build());
			}
		});
		root.addProperty("type", "minecraft:sequence");
		return this;
	}

	public SurfaceRulesBuilder condition(SurfaceRulesBuilder ifTrue, SurfaceRulesBuilder theRun) {
		this.with("if_true", JsonObject::new, ifTrue::buildTo);
		this.with("then_run", JsonObject::new, theRun::buildTo);
		root.addProperty("type", "minecraft:condition");
		return this;
	}

	public SurfaceRulesBuilder condition(Map.Entry<String, String> ifTrue, SurfaceRulesBuilder theRun) {
		this.with("if_true", JsonObject::new, jsonObject -> jsonObject.addProperty(ifTrue.getKey(), ifTrue.getValue()));
		this.with("then_run", JsonObject::new, theRun::buildTo);
		root.addProperty("type", "minecraft:condition");
		return this;
	}

	public SurfaceRulesBuilder bandlands() {
		root.addProperty("type", "minecraft:bandlands");
		return this;
	}

	public SurfaceRulesBuilder block(StateDataBuilder builder) {
		with("result_type", JsonObject::new, builder::buildTo);
		return this;
	}

	public SurfaceRulesBuilder block(String name, Map.Entry<String, String> blockState) {
		JsonObject resultState = new JsonObject();
		resultState.addProperty("Name", name);

		JsonObject blockStateObject = new JsonObject();
		blockStateObject.addProperty(blockState.getKey(), blockState.getValue());
		resultState.add("Properties", blockStateObject);

		this.root.add("result_state", resultState);
		this.root.addProperty("type", "minecraft:block");
		return this;
	}

	public SurfaceRulesBuilder verticalGradient(String randomName, Map.Entry<String, Integer> trueAtAndBelow, Map.Entry<String, Integer> falseAtAndAbove) {
		root.addProperty("random_name", randomName);
		this.with("true_at_and_below", JsonObject::new, jsonObject -> jsonObject.addProperty(trueAtAndBelow.getKey(), trueAtAndBelow.getValue()));
		this.with("false_at_and_above", JsonObject::new, jsonObject -> jsonObject.addProperty(falseAtAndAbove.getKey(), falseAtAndAbove.getValue()));
		root.addProperty("type", "minecraft:vertical_gradient");
		return this;
	}

	public SurfaceRulesBuilder not(Processor<SurfaceRulesBuilder> rule) {
		this.with("invert", JsonObject::new, jsonObject -> rule.process(new SurfaceRulesBuilder()).buildTo(jsonObject));
		root.addProperty("type", "minecraft:not");
		return this;
	}

	public SurfaceRulesBuilder biome(ResourceKey<Biome>... biomes) {
		List<ResourceLocation> values = new ArrayList<>();
		for (ResourceKey<Biome> biome : biomes) {
			values.add(biome.location());
		}
		return this.biome(values.toArray(new ResourceLocation[0]));
	}

	public SurfaceRulesBuilder biome(ResourceLocation... biomes) {
		List<String> values = new ArrayList<>();
		for (ResourceLocation biome : biomes) {
			values.add(biome.toString());
		}
		return this.biome(values.toArray(new String[0]));
	}

	public SurfaceRulesBuilder biome(String... biomes) {
		jsonArray("biome_is", jsonArrayBuilder -> {
			for (String biome : biomes) {
				jsonArrayBuilder.add(biome);
			}
		});
		root.addProperty("type", "minecraft:biome");
		return this;
	}

	public SurfaceRulesBuilder yAbove(Map.Entry<String, Integer> yPos, int surfaceDepthMultiplier, boolean addStoneDepth) {
		this.with("anchor", JsonObject::new, jsonObject -> jsonObject.addProperty(yPos.getKey(), yPos.getValue()));
		root.addProperty("surface_depth_multiplier", surfaceDepthMultiplier);
		root.addProperty("add_stone_depth", addStoneDepth);
		root.addProperty("type", "minecraft:y_above");
		return this;
	}

	public SurfaceRulesBuilder water(int offset, int surfaceDepthMultiplier, boolean addStoneDepth) {
		root.addProperty("offset", offset);
		root.addProperty("surface_depth_multiplier", surfaceDepthMultiplier);
		root.addProperty("add_stone_depth", addStoneDepth);
		root.addProperty("type", "minecraft:water");
		return this;
	}

	public SurfaceRulesBuilder noiseThreshold(String noiseId, double min, double max) {
		root.addProperty("noise", noiseId);
		root.addProperty("min_threshold", min);
		root.addProperty("max_threshold", max);
		root.addProperty("type", "minecraft:noise_threshold");
		return this;
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
