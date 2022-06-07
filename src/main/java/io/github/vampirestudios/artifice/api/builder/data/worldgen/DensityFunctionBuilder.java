package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters;

public class DensityFunctionBuilder extends TypedJsonObject {
	public DensityFunctionBuilder() {
		super(new JsonObject());
	}

	public static DensityFunctionBuilder shiftX(ResourceKey<DensityFunction> noise) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("shift_x", noise.location().toString());
		return builder;
	}

	public static DensityFunctionBuilder shiftX(Number number) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("shift_x", number);
		return builder;
	}

	public static DensityFunctionBuilder shiftY(ResourceKey<DensityFunction> noise) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("shift_y", noise.location().toString());
		return builder;
	}

	public static DensityFunctionBuilder shiftY(Number number) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("shift_y", number);
		return builder;
	}

	public static DensityFunctionBuilder shiftZ(ResourceKey<DensityFunction> noise) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("shift_z", noise.location().toString());
		return builder;
	}

	public static DensityFunctionBuilder shiftZ(Number number) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("shift_z", number);
		return builder;
	}

	/**
	 * Calculates the absolute value of another density function.
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder abs(DensityFunctionBuilder argument) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:abs");
		builder.add("argument", argument.build());
		return builder;
	}

	/**
	 * Adds two density functions together.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder add(DensityFunctionBuilder argument1, DensityFunctionBuilder argument2) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:add");
		builder.add("argument1", argument1.build());
		builder.add("argument2", argument2.build());
		return builder;
	}

	/**
	 * Adds two density functions together.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder add(Number argument1, DensityFunctionBuilder argument2) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:add");
		builder.add("argument1", argument1);
		builder.add("argument2", argument2.build());
		return builder;
	}

	/**
	 * Adds two density functions together.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder add(DensityFunctionBuilder argument1, Number argument2) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:add");
		builder.add("argument1", argument1.build());
		builder.add("argument2", argument2);
		return builder;
	}

	/**
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder blendAlpha() {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:blend_alpha");
		return builder;
	}

	/**
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder blendDensity(DensityFunctionBuilder argument) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:blend_density");
		builder.add("argument", argument.build());
		return builder;
	}

	/**
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder blendOffset() {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:blend_offset");
		return builder;
	}

	/**
	 * Only computes the input density once for each column, at Y=0
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder cache2d(DensityFunctionBuilder argument) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:cache_2d");
		builder.add("argument", argument.build());
		return builder;
	}

	/**
	 * Only computes the input density once for each column, at Y=0
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder cache2d(ResourceKey<DensityFunction> argument) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:cache_2d");
		builder.add("argument", argument.location().toString());
		return builder;
	}

	/**
	 * Use in combination with {@link DensityFunctionBuilder#interpolated(DensityFunctionBuilder)}
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder cacheAllInCell(DensityFunctionBuilder argument) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:cache_all_in_cell");
		builder.add("argument", argument.build());
		return builder;
	}

	/**
	 * If this density function is referenced twice, it is only computed once per block position
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder cacheOnce(DensityFunctionBuilder argument) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:cache_once");
		builder.add("argument", argument.build());
		return builder;
	}

	/**
	 * Clamps the input between two values
	 *
	 * @param input The input density function
	 * @param min   The lower bound
	 * @param max   The upper bound
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder clamp(DensityFunctionBuilder input, double min, double max) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:clamp");
		builder.add("input", input.build());
		builder.add("min", min);
		builder.add("max", max);
		return builder;
	}

	/**
	 * A constant value <code>{"type": "constant", "argument": 2}</code> is equivalent to <code>2</code>
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder constant(Number argument) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:constant");
		builder.add("argument", argument);
		return builder;
	}

	/**
	 * Cubes the input. (<code>x^3</code>)
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder cube(DensityFunctionBuilder argument) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:cube");
		builder.add("argument", argument.build());
		return builder;
	}

	/**
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder endIsland() {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:end_island");
		return builder;
	}

	/**
	 * Similar to {@link DensityFunctionBuilder#cache2d(DensityFunctionBuilder)} in that it only computes the input once for each column,
	 * but now at the first Y value that is requested.
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder flatCache(DensityFunctionBuilder argument) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:flat_cache");
		builder.add("argument", argument.build());
		return builder;
	}

	/**
	 * If the input is negative, returns half of the input. Otherwise, it returns the input. (<code>x &lt; 0 ? x/2 : x</code>)
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder halfNegative(DensityFunctionBuilder argument) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:half_negative");
		builder.add("argument", argument.build());
		return builder;
	}

	/**
	 * Computes the input density at each of the 8 corners of a cell and interpolates between them. The size of a cell if determined by
	 * <code>size_horizontal * 4</code> and <code>size_vertical * 4</code>.
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder interpolated(DensityFunctionBuilder argument) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:interpolated");
		builder.add("argument", argument.build());
		return builder;
	}

	/**
	 * Returns the maximum of two density functions.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder max(DensityFunctionBuilder argument1, DensityFunctionBuilder argument2) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:max");
		builder.add("argument1", argument1.build());
		builder.add("argument2", argument2.build());
		return builder;
	}

	/**
	 * Returns the minimum of two density functions.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder min(DensityFunctionBuilder argument1, DensityFunctionBuilder argument2) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:min");
		builder.add("argument1", argument1.build());
		builder.add("argument2", argument2.build());
		return builder;
	}

	/**
	 * Multiplies two density functions.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder mul(DensityFunctionBuilder argument1, DensityFunctionBuilder argument2) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:mul");
		builder.add("argument1", argument1.build());
		builder.add("argument2", argument2.build());
		return builder;
	}

	/**
	 * Multiplies two density functions.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder mul(Number argument1, DensityFunctionBuilder argument2) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:mul");
		builder.add("argument1", argument1);
		builder.add("argument2", argument2.build());
		return builder;
	}

	/**
	 * Multiplies two density functions.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder mul(DensityFunctionBuilder argument1, Number argument2) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:mul");
		builder.add("argument1", argument1.build());
		builder.add("argument2", argument2);
		return builder;
	}

	/**
	 * Multiplies two density functions.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder mul(ResourceKey<DensityFunction> argument1, DensityFunctionBuilder argument2) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:mul");
		builder.add("argument1", argument1.location().toString());
		builder.add("argument2", argument2.build());
		return builder;
	}

	/**
	 * Multiplies two density functions.
	 *
	 * @param argument1 The first density function
	 * @param argument2 The second density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder mul(DensityFunctionBuilder argument1, ResourceKey<DensityFunction> argument2) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:mul");
		builder.add("argument1", argument1.build());
		builder.add("argument2", argument2.location().toString());
		return builder;
	}

	/**
	 * The noise density function samples a {@link NoiseParameters}
	 *
	 * @param noise    A reference to a <code>worldgen/noise</code> file
	 * @param xz_scale Scales the X and Z inputs before sampling
	 * @param y_scale  Scales the Y input before sampling
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder noise(ResourceKey<NoiseParameters> noise, double xz_scale, double y_scale) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:noise");
		builder.add("noise", noise.location().toString());
		builder.add("xz_scale", xz_scale);
		builder.add("y_scale", y_scale);
		return builder;
	}

	/**
	 * Uses a different kind of noise than {@link DensityFunctionBuilder#noise(ResourceKey, double, double)}
	 *
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder oldBlendedNoise(double xz_scale, double y_scale, double xz_factor, double y_factor, double smear_scale_multiplier) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:old_blended_noise");
		builder.add("xz_scale", xz_scale);
		builder.add("y_scale", y_scale);
		builder.add("xz_factor", xz_factor);
		builder.add("y_factor", y_factor);
		builder.add("smear_scale_multiplier", smear_scale_multiplier);
		return builder;
	}

	/**
	 * If the input is negative, returns a quarter of the input. Otherwise, it returns the input. (<code>x &lt; 0 ? x/4 : x</code>)
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder quarterNegative(DensityFunctionBuilder argument) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:quarter_negative");
		builder.add("argument", argument.build());
		return builder;
	}

	/**
	 * Computes the input value, and depending on that result returns one of two other density functions. Basically an if-then-else statement.
	 *
	 * @param input          The input density function
	 * @param minInclusive   The lower bound of the range
	 * @param maxExclusive   The upper bound of the range
	 * @param whenInRange    Density function that will be returned when the input is inside the range
	 * @param whenOutOfRange Density function that will be returned When the input is outside the range
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder rangeChoice(DensityFunctionBuilder input, HeightProviderBuilders minInclusive, HeightProviderBuilders maxExclusive,
													 DensityFunctionBuilder whenInRange, DensityFunctionBuilder whenOutOfRange) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:range_choice");
		builder.add("noise", input.build());
		builder.add("min_inclusive", minInclusive.build());
		builder.add("max_exclusive", maxExclusive.build());
		builder.add("when_in_range", whenInRange.build());
		builder.add("when_out_of_range", whenOutOfRange.build());
		return builder;
	}

	/**
	 * Samples a noise at (<code>x/4, y/4, z/4</code>)
	 *
	 * @param argument A reference to a <code>worldgen/noise</code> file
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder shift(ResourceKey<NoiseParameters> argument) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("shift", argument.location().toString());
		return builder;
	}

	/**
	 * Samples a noise at (<code>x/4, 0, z/4</code>)
	 *
	 * @param argument A reference to a <code>worldgen/noise</code> file
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder shiftA(ResourceKey<NoiseParameters> argument) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("shift_a", argument.location().toString());
		return builder;
	}

	/**
	 * Samples a noise at (<code>z/4, x/4, 0</code>)
	 *
	 * @param argument A reference to a <code>worldgen/noise</code> file
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder shiftB(ResourceKey<NoiseParameters> argument) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("shift_b", argument.location().toString());
		return builder;
	}

	/**
	 * Samples a noise at (<code>x/4, y/4, z/4</code>)
	 *
	 * @param noise    A reference to a <code>worldgen/noise</code> file
	 * @param xz_scale Scales the X and Z inputs before sampling
	 * @param y_scale  Scales the Y input before sampling
	 * @param shift_x  Density function used to offset the X input
	 * @param shift_y  Density function used to offset the X input
	 * @param shift_z  Density function used to offset the X input
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder shiftedNoise(ResourceKey<NoiseParameters> noise, double xz_scale, double y_scale,
													  DensityFunctionBuilder shift_x, DensityFunctionBuilder shift_y,
													  DensityFunctionBuilder shift_z) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:range_choice");
		builder.add("noise", noise.location().toString());
		builder.add("xz_scale", xz_scale);
		builder.add("y_scale", y_scale);
		builder.add("shift_x", shift_x.build());
		builder.add("shift_y", shift_y.build());
		builder.add("shift_z", shift_z.build());
		return builder;
	}

	/**
	 * Squares the input. (<code>x^2</code>)
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder square(DensityFunctionBuilder argument) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:square");
		builder.add("argument", argument.build());
		return builder;
	}

	/**
	 * First clamps the input between <code>-1</code> and <code>1</code>, then transforms it using <code>x/2 - x*x*x/24</code>
	 *
	 * @param argument The input density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder squeeze(DensityFunctionBuilder argument) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:squeeze");
		builder.add("argument", argument.build());
		return builder;
	}

	/**
	 * @param rarityValueMapper One of <code>type_1</code> or <code>type_2</code>
	 * @param noise             A reference to a <code>worldgen/noise</code> file
	 * @param input             The input density function
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder weirdScaledSampler(RarityValueMapperTypes rarityValueMapper, ResourceKey<NoiseParameters> noise,
															DensityFunctionBuilder input) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:blend_density");
		builder.add("rarity_value_mapper", rarityValueMapper.toString());
		builder.add("noise", noise.location().toString());
		builder.add("input", input.build());
		return builder;
	}

	/**
	 * Returns the Y position after mapping it to a range.
	 *
	 * @param fromValue The value to map a <code>from_y</code> to
	 * @param toValue   The value to map a <code>to_y</code> to
	 * @return {@link DensityFunctionBuilder}
	 **/
	public static DensityFunctionBuilder yClampedGradient(double fromY, double toY, double fromValue, double toValue) {
		DensityFunctionBuilder builder = new DensityFunctionBuilder();
		builder.add("type", "minecraft:y_clamped_gradient");
		builder.add("from_y", fromY);
		builder.add("to_y", toY);
		builder.add("from_value", fromValue);
		builder.add("to_value", toValue);
		return builder;
	}

	public enum RarityValueMapperTypes {
		type_1,
		type_2
	}

}