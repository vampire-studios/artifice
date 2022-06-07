package io.github.vampirestudios.artifice.api.builder.data.dimension;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;

public class NoiseConfigBuilder extends TypedJsonObject {
	public NoiseConfigBuilder() {
		super(new JsonObject());
	}

	public static NoiseConfigBuilder noiseConfig(int minY, int height, int sizeHorizontal, int sizeVertical) {
		NoiseConfigBuilder builder = new NoiseConfigBuilder();
		return builder.minimumY(minY).height(height).sizeHorizontal(sizeHorizontal).sizeVertical(sizeVertical);
	}

	public NoiseConfigBuilder height(int height) {
		NoiseConfigBuilder builder = new NoiseConfigBuilder();
		builder.add("height", height);
		return builder;
	}

	public NoiseConfigBuilder minimumY(int minimumY) {
		NoiseConfigBuilder builder = new NoiseConfigBuilder();
		builder.add("min_y", minimumY);
		return builder;
	}

	public NoiseConfigBuilder sizeHorizontal(int sizeHorizontal) {
		NoiseConfigBuilder builder = new NoiseConfigBuilder();
		if (sizeHorizontal > 4)
			throw new IllegalArgumentException("sizeHorizontal can't be higher than 4! Found " + sizeHorizontal);
		if (sizeHorizontal < 1)
			throw new IllegalArgumentException("sizeHorizontal can't be smaller than 1! Found " + sizeHorizontal);
		if (sizeHorizontal == 3)
			throw new IllegalArgumentException("sizeHorizontal should not be 3! See https://gist.github.com/misode/b83bfe4964e6bf53b2dd31b22ee94157 for information of why it should not be 3");
		builder.add("size_horizontal", sizeHorizontal);
		return builder;
	}

	public NoiseConfigBuilder sizeVertical(int sizeVertical) {
		NoiseConfigBuilder builder = new NoiseConfigBuilder();
		if (sizeVertical > 4)
			throw new IllegalArgumentException("sizeVertical can't be higher than 4! Found " + sizeVertical);
		if (sizeVertical < 1)
			throw new IllegalArgumentException("sizeVertical can't be smaller than 1! Found " + sizeVertical);
		builder.add("size_vertical", sizeVertical);
		return builder;
	}

	public NoiseConfigBuilder densityFactor(double densityFactor) {
		NoiseConfigBuilder builder = new NoiseConfigBuilder();
		builder.add("density_factor", densityFactor);
		return builder;
	}

	public NoiseConfigBuilder densityOffset(double densityOffset) {
		NoiseConfigBuilder builder = new NoiseConfigBuilder();
		if (densityOffset > 1)
			throw new IllegalArgumentException("densityOffset can't be higher than 1! Found " + densityOffset);
		if (densityOffset < -1)
			throw new IllegalArgumentException("densityOffset can't be smaller than -1! Found " + densityOffset);
		builder.add("density_offset", densityOffset);
		return builder;
	}

	public NoiseConfigBuilder simplexSurfaceNoise(boolean simplexSurfaceNoise) {
		NoiseConfigBuilder builder = new NoiseConfigBuilder();
		builder.add("simplex_surface_noise", simplexSurfaceNoise);
		return builder;
	}

	public NoiseConfigBuilder randomDensityOffset(boolean randomDensityOffset) {
		NoiseConfigBuilder builder = new NoiseConfigBuilder();
		builder.add("random_density_offset", randomDensityOffset);
		return builder;
	}

	public static JsonObject spline(String noise, JsonObject... points) {
		JsonObject obj = new JsonObject();
		obj.addProperty("coordinate", noise);
		JsonArray ary = new JsonArray();
		for (JsonObject arOb : points) {
			ary.add(arOb);
		}
		obj.add("points", ary);
		return obj;
	}

	public static JsonObject point(double location, double derivative, Object value) {
		JsonObject obj = new JsonObject();
		obj.addProperty("location", location);
		obj.addProperty("derivative", derivative);
		if (value instanceof JsonObject) obj.add("value", (JsonObject) value);
		else if (value instanceof Double) obj.addProperty("value", (double) value);
		else throw new IllegalArgumentException("value must be spline or double");
		return obj;
	}

}
