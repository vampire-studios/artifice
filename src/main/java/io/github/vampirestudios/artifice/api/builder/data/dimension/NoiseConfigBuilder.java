package io.github.vampirestudios.artifice.api.builder.data.dimension;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;

public class NoiseConfigBuilder extends TypedJsonBuilder<JsonObject> {
	public NoiseConfigBuilder() {
		super(new JsonObject(), j -> j);
	}

	public NoiseConfigBuilder height(int height) {
		this.root.addProperty("height", height);
		return this;
	}

	public NoiseConfigBuilder minimumY(int minimumY) {
		this.root.addProperty("min_y", minimumY);
		return this;
	}

	public NoiseConfigBuilder sizeHorizontal(int sizeHorizontal) {
		if (sizeHorizontal > 4)
			throw new IllegalArgumentException("sizeHorizontal can't be higher than 4! Found " + sizeHorizontal);
		if (sizeHorizontal < 1)
			throw new IllegalArgumentException("sizeHorizontal can't be smaller than 1! Found " + sizeHorizontal);
		if (sizeHorizontal == 3)
			throw new IllegalArgumentException("sizeHorizontal should not be 3! See https://gist.github.com/misode/b83bfe4964e6bf53b2dd31b22ee94157 for information of why it should not be 3");
		this.root.addProperty("size_horizontal", sizeHorizontal);
		return this;
	}

	public NoiseConfigBuilder sizeVertical(int sizeVertical) {
		if (sizeVertical > 4)
			throw new IllegalArgumentException("sizeVertical can't be higher than 4! Found " + sizeVertical);
		if (sizeVertical < 1)
			throw new IllegalArgumentException("sizeVertical can't be smaller than 1! Found " + sizeVertical);
		this.root.addProperty("size_vertical", sizeVertical);
		return this;
	}

	public NoiseConfigBuilder densityFactor(double densityFactor) {
		this.root.addProperty("density_factor", densityFactor);
		return this;
	}

	public NoiseConfigBuilder densityOffset(double densityOffset) {
		if (densityOffset > 1)
			throw new IllegalArgumentException("densityOffset can't be higher than 1! Found " + densityOffset);
		if (densityOffset < -1)
			throw new IllegalArgumentException("densityOffset can't be smaller than -1! Found " + densityOffset);
		this.root.addProperty("density_offset", densityOffset);
		return this;
	}

	public NoiseConfigBuilder simplexSurfaceNoise(boolean simplexSurfaceNoise) {
		this.root.addProperty("simplex_surface_noise", simplexSurfaceNoise);
		return this;
	}

	public NoiseConfigBuilder randomDensityOffset(boolean randomDensityOffset) {
		this.root.addProperty("random_density_offset", randomDensityOffset);
		return this;
	}

	public JsonObject spline(String noise, JsonObject... points) {
		JsonObject obj = new JsonObject();
		obj.addProperty("coordinate", noise);
		JsonArray ary = new JsonArray();
		for (JsonObject arOb : points) {
			ary.add(arOb);
		}
		obj.add("points", ary);
		return obj;
	}

	public JsonObject point(double location, double derivative, Object value, int aa) {
		JsonObject obj = new JsonObject();
		obj.addProperty("location", location);
		obj.addProperty("derivative", derivative);
		if (value instanceof JsonObject) obj.add("value", (JsonObject) value);
		else if (value instanceof Double) obj.addProperty("value", (double) value);
		else throw new IllegalArgumentException("value must be spline or double");
		return obj;
	}

	public JsonObject point(double location, double derivative, JsonObject value) {
		return point(location, derivative, value, 0);
	}

	public JsonObject point(double location, double derivative, double value) {
		return point(location, derivative, value, 0);
	}

}
