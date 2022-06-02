package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.HeightProviderBuilders.UniformHeightProviderBuilder;
import io.github.vampirestudios.artifice.api.resource.JsonResource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters;

public class DensityFunctionBuilder extends TypedJsonBuilder<JsonResource<JsonObject>> {
	public DensityFunctionBuilder() {
		super(new JsonObject(), JsonResource::new);
	}

	@Deprecated
	public DensityFunctionBuilder shiftX(ResourceKey<DensityFunction> noise) {
		root.addProperty("shift_x", noise.location().toString());
		return this;
	}

	@Deprecated
	public DensityFunctionBuilder shiftX(Number number) {
		root.addProperty("shift_x", number);
		return this;
	}

	@Deprecated
	public DensityFunctionBuilder shiftY(ResourceKey<DensityFunction> noise) {
		root.addProperty("shift_y", noise.location().toString());
		return this;
	}

	@Deprecated
	public DensityFunctionBuilder shiftY(Number number) {
		root.addProperty("shift_y", number);
		return this;
	}

	@Deprecated
	public DensityFunctionBuilder shiftZ(ResourceKey<DensityFunction> noise) {
		root.addProperty("shift_z", noise.location().toString());
		return this;
	}

	@Deprecated
	public DensityFunctionBuilder shiftZ(Number number) {
		root.addProperty("shift_z", number);
		return this;
	}

	/**
	 * Calculates the absolute value of another density function.
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 *
	 * **/
	public DensityFunctionBuilder abs(DensityFunctionBuilder argument) {
		this.root.addProperty("type", "minecraft:abs");
		this.with("argument", JsonObject::new, argument::buildTo);
		return this;
	}

	/**
	 * Adds two density functions together.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 *
	 * **/
	public DensityFunctionBuilder add(DensityFunctionBuilder argument1, DensityFunctionBuilder argument2) {
		this.root.addProperty("type", "minecraft:add");
		this.with("argument1", JsonObject::new, argument1::buildTo);
		this.with("argument2", JsonObject::new, argument2::buildTo);
		return this;
	}

	/**
	 * Adds two density functions together.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 *
	 * **/
	public DensityFunctionBuilder add(Number argument1, DensityFunctionBuilder argument2) {
		this.root.addProperty("type", "minecraft:add");
		this.jsonNumber("argument1", argument1);
		this.with("argument2", JsonObject::new, argument2::buildTo);
		return this;
	}

	/**
	 * Adds two density functions together.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 *
	 * **/
	public DensityFunctionBuilder add(DensityFunctionBuilder argument1, Number argument2) {
		this.root.addProperty("type", "minecraft:add");
		this.with("argument1", JsonObject::new, argument1::buildTo);
		this.jsonNumber("argument2", argument2);
		return this;
	}

	/**
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder blendAlpha() {
		this.jsonString("type", "minecraft:blend_alpha");
		return this;
	}

	/**
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder blendDensity(DensityFunctionBuilder argument) {
		this.jsonString("type", "minecraft:blend_density");
		this.with("argument", JsonObject::new, argument::buildTo);
		return this;
	}

	/**
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder blendOffset() {
		this.jsonString("type", "minecraft:blend_offset");
		return this;
	}

	/**
	 * Only computes the input density once for each column, at Y=0
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder cache2d(DensityFunctionBuilder argument) {
		this.jsonString("type", "minecraft:cache_2d");
		this.with("argument", JsonObject::new, argument::buildTo);
		return this;
	}

	/**
	 * Only computes the input density once for each column, at Y=0
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder cache2d(ResourceKey<DensityFunction> argument) {
		this.jsonString("type", "minecraft:cache_2d");
		this.jsonString("argument", argument.location().toString());
		return this;
	}

	/**
	 * Use in combination with {@link DensityFunctionBuilder#interpolated(DensityFunctionBuilder)}
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder cacheAllInCell(DensityFunctionBuilder argument) {
		this.jsonString("type", "minecraft:cache_all_in_cell");
		this.with("argument", JsonObject::new, argument::buildTo);
		return this;
	}

	/**
	 * If this density function is referenced twice, it is only computed once per block position
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder cacheOnce(DensityFunctionBuilder argument) {
		this.jsonString("type", "minecraft:cache_once");
		this.with("argument", JsonObject::new, argument::buildTo);
		return this;
	}

	/**
	 * Clamps the input between two values
	 *
	 * @param input The input density function
	 * @param min The lower bound
	 * @param max The upper bound
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder clamp(DensityFunctionBuilder input, double min, double max) {
		this.jsonString("type", "minecraft:clamp");
		this.with("input", JsonObject::new, input::buildTo);
		this.jsonNumber("min", min);
		this.jsonNumber("max", max);
		return this;
	}

	/**
	 * A constant value <code>{"type": "constant", "argument": 2}</code> is equivalent to <code>2</code>
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder constant(Number argument) {
		this.jsonString("type", "minecraft:constant");
		this.jsonNumber("argument", argument);
		return this;
	}

	/**
	 * Cubes the input. (<code>x^3</code>)
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder cube(DensityFunctionBuilder argument) {
		this.jsonString("type", "minecraft:cube");
		this.with("argument", JsonObject::new, argument::buildTo);
		return this;
	}

	/**
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder endIsland() {
		this.jsonString("type", "minecraft:end_island");
		return this;
	}

	/**
	 * Similar to {@link DensityFunctionBuilder#cache2d(DensityFunctionBuilder)} in that it only computes the input once for each column,
	 * but now at the first Y value that is requested.
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder flatCache(DensityFunctionBuilder argument) {
		this.jsonString("type", "minecraft:flat_cache");
		this.with("argument", JsonObject::new, argument::buildTo);
		return this;
	}

	/**
	 * If the input is negative, returns half of the input. Otherwise, it returns the input. (<code>x &lt; 0 ? x/2 : x</code>)
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder halfNegative(DensityFunctionBuilder argument) {
		this.jsonString("type", "minecraft:half_negative");
		this.with("argument", JsonObject::new, argument::buildTo);
		return this;
	}

	/**
	 * Computes the input density at each of the 8 corners of a cell and interpolates between them. The size of a cell if determined by
	 * <code>size_horizontal * 4</code> and <code>size_vertical * 4</code>.
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder interpolated(DensityFunctionBuilder argument) {
		this.jsonString("type", "minecraft:interpolated");
		this.with("argument", JsonObject::new, argument::buildTo);
		return this;
	}

	/**
	 * Returns the maximum of two density functions.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder max(DensityFunctionBuilder argument1, DensityFunctionBuilder argument2) {
		this.root.addProperty("type", "minecraft:max");
		this.with("argument1", JsonObject::new, argument1::buildTo);
		this.with("argument2", JsonObject::new, argument2::buildTo);
		return this;
	}

	/**
	 * Returns the minimum of two density functions.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder min(DensityFunctionBuilder argument1, DensityFunctionBuilder argument2) {
		this.root.addProperty("type", "minecraft:min");
		this.with("argument1", JsonObject::new, argument1::buildTo);
		this.with("argument2", JsonObject::new, argument2::buildTo);
		return this;
	}

	/**
	 * Multiplies two density functions.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder mul(DensityFunctionBuilder argument1, DensityFunctionBuilder argument2) {
		this.root.addProperty("type", "minecraft:mul");
		this.with("argument1", JsonObject::new, argument1::buildTo);
		this.with("argument2", JsonObject::new, argument2::buildTo);
		return this;
	}

	/**
	 * Multiplies two density functions.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder mul(Number argument1, DensityFunctionBuilder argument2) {
		this.root.addProperty("type", "minecraft:mul");
		this.jsonNumber("argument1", argument1);
		this.with("argument2", JsonObject::new, argument2::buildTo);
		return this;
	}

	/**
	 * Multiplies two density functions.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder mul(DensityFunctionBuilder argument1, Number argument2) {
		this.root.addProperty("type", "minecraft:mul");
		this.with("argument1", JsonObject::new, argument1::buildTo);
		this.jsonNumber("argument2", argument2);
		return this;
	}

	/**
	 * Multiplies two density functions.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder mul(ResourceKey<DensityFunction> argument1, DensityFunctionBuilder argument2) {
		this.root.addProperty("type", "minecraft:mul");
		this.jsonString("argument1", argument1.location().toString());
		this.with("argument2", JsonObject::new, argument2::buildTo);
		return this;
	}

	/**
	 * Multiplies two density functions.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder mul(DensityFunctionBuilder argument1, ResourceKey<DensityFunction> argument2) {
		this.root.addProperty("type", "minecraft:mul");
		this.with("argument1", JsonObject::new, argument1::buildTo);
		this.jsonString("argument2", argument2.location().toString());
		return this;
	}

	/**
	 * The noise density function samples a {@link NoiseParameters}
	 *
	 * @param noise A reference to a <code>worldgen/noise</code> file
	 * @param xz_scale Scales the X and Z inputs before sampling
	 * @param y_scale Scales the Y input before sampling
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder noise(ResourceKey<NoiseParameters> noise, double xz_scale, double y_scale) {
		this.jsonString("type", "minecraft:noise");
		this.jsonString("noise", noise.location().toString());
		this.jsonNumber("xz_scale", xz_scale);
		this.jsonNumber("y_scale", y_scale);
		return this;
	}

	/**
	 * Uses a different kind of noise than {@link DensityFunctionBuilder#noise(ResourceKey, double, double)}
	 *
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder oldBlendedNoise(double xz_scale, double y_scale, double xz_factor, double y_factor, double smear_scale_multiplier) {
		this.jsonString("type", "minecraft:old_blended_noise");
		this.jsonNumber("xz_scale", xz_scale);
		this.jsonNumber("y_scale", y_scale);
		this.jsonNumber("xz_factor", xz_factor);
		this.jsonNumber("y_factor", y_factor);
		this.jsonNumber("smear_scale_multiplier", smear_scale_multiplier);
		return this;
	}

	/**
	 * If the input is negative, returns a quarter of the input. Otherwise, it returns the input. (<code>x &lt; 0 ? x/4 : x</code>)
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder quarterNegative(DensityFunctionBuilder argument) {
		this.root.addProperty("type", "minecraft:quarter_negative");
		this.with("argument", JsonObject::new, argument::buildTo);
		return this;
	}

	/**
	 * Computes the input value, and depending on that result returns one of two other density functions. Basically an if-then-else statement.
	 *
	 * @param input The input density function
	 * @param minInclusive The lower bound of the range
	 * @param maxExclusive The upper bound of the range
	 * @param whenInRange Density function that will be returned when the input is inside the range
	 * @param whenOutOfRange Density function that will be returned When the input is outside the range
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder rangeChoice(DensityFunctionBuilder input, UniformHeightProviderBuilder minInclusive, UniformHeightProviderBuilder maxExclusive,
											  DensityFunctionBuilder whenInRange, DensityFunctionBuilder whenOutOfRange) {
		this.jsonString("type", "minecraft:range_choice");
		this.with("noise", JsonObject::new, input::buildTo);
		this.with("min_inclusive", JsonObject::new, minInclusive::buildTo);
		this.with("max_exclusive", JsonObject::new, maxExclusive::buildTo);
		this.with("when_in_range", JsonObject::new, whenInRange::buildTo);
		this.with("when_out_of_range", JsonObject::new, whenOutOfRange::buildTo);
		return this;
	}

	/**
	 * Samples a noise at (<code>x/4, y/4, z/4</code>)
	 *
	 * @param argument A reference to a <code>worldgen/noise</code> file
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder shift(ResourceKey<NoiseParameters> argument) {
		root.addProperty("shift", argument.location().toString());
		return this;
	}

	/**
	 * Samples a noise at (<code>x/4, 0, z/4</code>)
	 *
	 * @param argument A reference to a <code>worldgen/noise</code> file
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder shiftA(ResourceKey<NoiseParameters> argument) {
		root.addProperty("shift_a", argument.location().toString());
		return this;
	}

	/**
	 * Samples a noise at (<code>z/4, x/4, 0</code>)
	 *
	 * @param argument A reference to a <code>worldgen/noise</code> file
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder shiftB(ResourceKey<NoiseParameters> argument) {
		root.addProperty("shift_b", argument.location().toString());
		return this;
	}

	/**
	 * Samples a noise at (<code>x/4, y/4, z/4</code>)
	 *
	 * @param noise A reference to a <code>worldgen/noise</code> file
	 * @param xz_scale Scales the X and Z inputs before sampling
	 * @param y_scale Scales the Y input before sampling
	 * @param shift_x Density function used to offset the X input
	 * @param shift_y Density function used to offset the X input
	 * @param shift_z Density function used to offset the X input
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder shiftedNoise(ResourceKey<NoiseParameters> noise, double xz_scale, double y_scale,
											   DensityFunctionBuilder shift_x, DensityFunctionBuilder shift_y,
											   DensityFunctionBuilder shift_z) {
		this.jsonString("type", "minecraft:range_choice");
		this.jsonString("noise", noise.location().toString());
		this.jsonNumber("xz_scale", xz_scale);
		this.jsonNumber("y_scale", y_scale);
		this.with("shift_x", JsonObject::new, shift_x::buildTo);
		this.with("shift_y", JsonObject::new, shift_y::buildTo);
		this.with("shift_z", JsonObject::new, shift_z::buildTo);
		return this;
	}

	/**
	 * Squares the input. (<code>x^2</code>)
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder square(DensityFunctionBuilder argument) {
		this.jsonString("type", "minecraft:square");
		this.with("argument", JsonObject::new, argument::buildTo);
		return this;
	}

	/**
	 * First clamps the input between <code>-1</code> and <code>1</code>, then transforms it using <code>x/2 - x*x*x/24</code>
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder squeeze(DensityFunctionBuilder argument) {
		this.jsonString("type", "minecraft:squeeze");
		this.with("argument", JsonObject::new, argument::buildTo);
		return this;
	}

	/**
	 * @param rarityValueMapper One of <code>type_1</code> or <code>type_2</code>
	 * @param noise A reference to a <code>worldgen/noise</code> file
	 * @param input The input density function
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder weirdScaledSampler(RarityValueMapperTypes rarityValueMapper, ResourceKey<NoiseParameters> noise,
													 DensityFunctionBuilder input) {
		this.jsonString("type", "minecraft:blend_density");
		this.jsonString("rarity_value_mapper", rarityValueMapper.toString());
		this.jsonString("noise", noise.location().toString());
		this.with("input", JsonObject::new, input::buildTo);
		return this;
	}

	/**
	 * Returns the Y position after mapping it to a range.
	 *
	 * @param fromValue The value to map a <code>from_y</code> to
	 * @param toValue The value to map a <code>to_y</code> to
	 * @return {@link DensityFunctionBuilder}
	 * **/
	public DensityFunctionBuilder yClampedGradient(double fromY, double toY, double fromValue, double toValue) {
		this.jsonString("type", "minecraft:y_clamped_gradient");
		this.jsonNumber("from_y", fromY);
		this.jsonNumber("to_y", toY);
		this.jsonNumber("from_value", fromValue);
		this.jsonNumber("to_value", toValue);
		return this;
	}

	public enum RarityValueMapperTypes {
		type_1,
		type_2
	}

}