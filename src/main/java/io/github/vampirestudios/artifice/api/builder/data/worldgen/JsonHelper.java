package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class JsonHelper {
	
	private JsonObject jsonObject;

	public JsonHelper(JsonObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public JsonHelper setJsonObject(JsonObject jsonObject) {
		this.jsonObject = jsonObject;
		return this;
	}

	public JsonHelper minMax(Number min, Number max) {
		number("min", min);
		number("max", max);
		return this;
	}

	public JsonHelper minMaxInclusiveExclusive(Number min, Number max) {
		number("min_inclusive", min);
		number("max_exclusive", max);
		return this;
	}

	public JsonHelper type(DensityType densityType) {
		jsonObject.addProperty("type", densityType.getName());
		return this;
	}

	public JsonHelper input(ResourceKey<DensityFunction> noise) {
		densityFunction("input", noise);
		return this;
	}

	public JsonHelper string(String name, String string) {
		jsonObject.addProperty(name, string);
		return this;
	}

	public JsonHelper noise(ResourceKey<NormalNoise.NoiseParameters> string) {
		jsonObject.addProperty("noise", string.location().toString());
		return this;
	}

	public JsonHelper noise(String name, ResourceKey<NormalNoise.NoiseParameters> string) {
		jsonObject.addProperty(name, string.location().toString());
		return this;
	}

	public JsonHelper densityFunction(String name, ResourceKey<DensityFunction> string) {
		jsonObject.addProperty(name, string.location().toString());
		return this;
	}

	public JsonHelper number(String name, Number number) {
		jsonObject.addProperty(name, number);
		return this;
	}

	public JsonHelper jsonObject(String name, JsonObject innerJsonObject) {
		jsonObject.add(name, innerJsonObject);
		return this;
	}

	public enum DensityType {
		INTERPOLATED("minecraft:interpolated"),
		BLEND_DENSITY("minecraft:blend_density"),
		SLIDE("minecraft:slide"),
		RANGE_CHOICE("minecraft:range_choice"),
		ADD("minecraft:add"),
		MIN("minecraft:min"),
		MAX("minecraft:max"),
		MUL("minecraft:mul"),
		ABS("minecraft:abs"),
		NOISE("minecraft:noise"),
		SHIFTED_NOISE("minecraft:shifted_noise"),
		OLD_BLENDED_NOISE("minecraft:old_blended_noise"),
		Y_CLAMPED_GRADIENT("minecraft:y_clamped_gradient"),
		CLAMP("minecraft:clamp"),
		SQUARE("minecraft:square"),
		SQUEEZE("minecraft:squeeze"),
		QUARTER_NEGATIVE("minecraft:quarter_negative"),
		CACHE_2D("minecraft:cache_2d");

		private final String name;

		DensityType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

}
