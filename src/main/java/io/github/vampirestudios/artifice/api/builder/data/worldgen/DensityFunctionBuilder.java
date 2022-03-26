package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.resource.JsonResource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class DensityFunctionBuilder extends TypedJsonObject {
	public DensityFunctionBuilder() {
		super(new JsonObject());
	}

	public DensityFunctionBuilder type(String type) {
		root.addProperty("type", type);
		return this;
	}

	public DensityFunctionBuilder type(JsonHelper.DensityType type) {
		root.addProperty("type", type.getName());
		return this;
	}

	public DensityFunctionBuilder noise(String noise) {
		root.addProperty("noise", noise);
		return this;
	}

	public DensityFunctionBuilder noise(ResourceKey<NormalNoise.NoiseParameters> noise) {
		root.addProperty("noise", noise.location().toString());
		return this;
	}

	public DensityFunctionBuilder xzScale(Number xzScale) {
		root.addProperty("xz_scale", xzScale);
		return this;
	}

	public DensityFunctionBuilder yScale(Number yScale) {
		root.addProperty("y_scale", yScale);
		return this;
	}

	public DensityFunctionBuilder shiftX(String noise) {
		root.addProperty("shift_x", noise);
		return this;
	}

	public DensityFunctionBuilder shiftX(Number shiftX) {
		root.addProperty("shift_x", shiftX);
		return this;
	}

	public DensityFunctionBuilder shiftX(ResourceKey<DensityFunction> noise) {
		root.addProperty("shift_x", noise.location().toString());
		return this;
	}

	public DensityFunctionBuilder shiftY(String noise) {
		root.addProperty("shift_y", noise);
		return this;
	}

	public DensityFunctionBuilder shiftY(Number shiftY) {
		root.addProperty("shift_y", shiftY);
		return this;
	}

	public DensityFunctionBuilder shiftY(ResourceKey<DensityFunction> noise) {
		root.addProperty("shift_y", noise.location().toString());
		return this;
	}

	public DensityFunctionBuilder shiftZ(String noise) {
		root.addProperty("shift_z", noise);
		return this;
	}

	public DensityFunctionBuilder shiftZ(Number shiftZ) {
		root.addProperty("shift_z", shiftZ);
		return this;
	}

	public DensityFunctionBuilder shiftZ(ResourceKey<DensityFunction> noise) {
		root.addProperty("shift_z", noise.location().toString());
		return this;
	}

}