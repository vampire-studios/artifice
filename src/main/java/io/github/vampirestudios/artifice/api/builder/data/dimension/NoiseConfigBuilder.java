package io.github.vampirestudios.artifice.api.builder.data.dimension;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;

public class NoiseConfigBuilder extends TypedJsonBuilder<JsonObject> {
    public NoiseConfigBuilder() {
        super(new JsonObject(), j->j);
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
        if (sizeHorizontal > 4) throw new IllegalArgumentException("sizeHorizontal can't be higher than 4! Found " + sizeHorizontal);
        if (sizeHorizontal < 1) throw new IllegalArgumentException("sizeHorizontal can't be smaller than 1! Found " + sizeHorizontal);
        if (sizeHorizontal == 3) throw new IllegalArgumentException("sizeHorizontal should not be 3! See https://gist.github.com/misode/b83bfe4964e6bf53b2dd31b22ee94157 for information of why it should not be 3");
        this.root.addProperty("size_horizontal", sizeHorizontal);
        return this;
    }

    public NoiseConfigBuilder sizeVertical(int sizeVertical) {
        if (sizeVertical > 4) throw new IllegalArgumentException("sizeVertical can't be higher than 4! Found " + sizeVertical);
        if (sizeVertical < 1) throw new IllegalArgumentException("sizeVertical can't be smaller than 1! Found " + sizeVertical);
        this.root.addProperty("size_vertical", sizeVertical);
        return this;
    }

    public NoiseConfigBuilder densityFactor(double densityFactor) {
        this.root.addProperty("density_factor", densityFactor);
        return this;
    }

    public NoiseConfigBuilder densityOffset(double densityOffset) {
        if (densityOffset > 1) throw new IllegalArgumentException("densityOffset can't be higher than 1! Found " + densityOffset);
        if (densityOffset < -1) throw new IllegalArgumentException("densityOffset can't be smaller than -1! Found " + densityOffset);
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

    /**
     * Build noise sampling config.
     */
    public NoiseConfigBuilder sampling(double xzScale, double yScale, double xzFactor, double yFactor) {
        JsonObject obj = new JsonObject();
        noiseChecker(xzScale,"xz_scale");
        noiseChecker(yScale,"y_scale");
        noiseChecker(xzFactor,"xz_factor");
        noiseChecker(yFactor,"y_factor");
        obj.addProperty("xz_scale", xzScale);
        obj.addProperty("y_scale", yScale);
        obj.addProperty("xz_factor", xzFactor);
        obj.addProperty("y_factor", yFactor);
        this.root.add("sampling",obj);
        return this;
    }

    void noiseChecker(double check, String name){
        if (check > 1000.0D) throw new IllegalArgumentException(name + " can't be higher than 1000.0D! Found " + check);
        if (check < 0.001D) throw new IllegalArgumentException(name + " can't be smaller than 0.001D! Found " + check);
    }

    /**
     * Build slide config.
     */
    private NoiseConfigBuilder slideConfig(String id, double target, int size, int offset) {
        JsonObject obj = new JsonObject();
        obj.addProperty("target", target);
        if (size > 255) throw new IllegalArgumentException("size can't be higher than 255! Found " + size);
        if (size < 0) throw new IllegalArgumentException("size can't be smaller than 0! Found " + size);
        obj.addProperty("size", size);
        obj.addProperty("offset", offset);
        this.root.add(id,obj);
        return this;
    }

    /**
     * Build top slide.
     */
    public NoiseConfigBuilder topSlide(double target, int size, int offset) {
        return this.slideConfig("top_slide", target, size, offset);
    }

    /**
     * Build bottom slide.
     */
    public NoiseConfigBuilder bottomSlide(double target, int size, int offset) {
        return this.slideConfig("bottom_slide", target, size, offset);
    }

    //todo a better system for this
    private NoiseConfigBuilder terrainShaper(Object offset, Object factor, Object jaggedness,int differentiator) {
        JsonObject obj = new JsonObject();
        if(offset instanceof JsonObject) obj.add("offset", (JsonObject)offset);
        else if (offset instanceof Double) obj.addProperty("offset",(double)offset);
        else throw new IllegalArgumentException("offset must be spline or double");

        if(factor instanceof JsonObject) obj.add("factor", (JsonObject)factor);
        else if (factor instanceof Double) obj.addProperty("factor",(double)factor);
        else throw new IllegalArgumentException("factor must be spline or double");

        if(jaggedness instanceof JsonObject) obj.add("jaggedness", (JsonObject)jaggedness);
        else if (jaggedness instanceof Double) obj.addProperty("jaggedness",(double)jaggedness);
        else throw new IllegalArgumentException("jaggedness must be spline or double");

        this.root.add("terrain_shaper",obj);
        return this;
    }
    public NoiseConfigBuilder terrainShaper(JsonObject offset, JsonObject factor, JsonObject jaggedness) { return terrainShaper(offset,factor,jaggedness,0);}
    public NoiseConfigBuilder terrainShaper(double offset, double factor, double jaggedness) { return terrainShaper(offset,factor,jaggedness,0);}
    public NoiseConfigBuilder terrainShaper(double offset, JsonObject factor, JsonObject jaggedness) { return terrainShaper(offset,factor,jaggedness,0);}
    public NoiseConfigBuilder terrainShaper(JsonObject offset, double factor, JsonObject jaggedness) { return terrainShaper(offset,factor,jaggedness,0);}
    public NoiseConfigBuilder terrainShaper(JsonObject offset, JsonObject factor, double jaggedness) { return terrainShaper(offset,factor,jaggedness,0);}
    public NoiseConfigBuilder terrainShaper(double offset, JsonObject factor, double jaggedness) { return terrainShaper(offset,factor,jaggedness,0);}
    public NoiseConfigBuilder terrainShaper(JsonObject offset, double factor, double jaggedness) { return terrainShaper(offset,factor,jaggedness,0);}
    public NoiseConfigBuilder terrainShaper(double offset, double factor, JsonObject jaggedness) { return terrainShaper(offset,factor,jaggedness,0);}

    public JsonObject spline(String noise, JsonObject... points) {
        JsonObject obj = new JsonObject();
        obj.addProperty("coordinate", noise);
        JsonArray ary = new JsonArray();
        for (JsonObject arOb: points) {
            ary.add(arOb);
        }
        obj.add ("points", ary);
        return obj;
    }

    public JsonObject point(double location, double derivative, Object value, int aa) {
        JsonObject obj = new JsonObject();
        obj.addProperty("location", location);
        obj.addProperty("derivative", derivative);
        if(value instanceof JsonObject)obj.add("value", (JsonObject)value);
        else if(value instanceof Double) obj.addProperty("value", (double)value);
        else throw new IllegalArgumentException("value must be spline or double");
        return obj;
    }
    public JsonObject point(double location, double derivative, JsonObject value){
        return point(location,derivative,value,0);
    }
    public JsonObject point(double location, double derivative, double value){
        return point(location,derivative,value,0);
    }

}
