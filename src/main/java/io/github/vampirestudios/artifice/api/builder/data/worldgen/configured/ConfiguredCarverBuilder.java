package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.HeightProviderBuilders;
import io.github.vampirestudios.artifice.api.resource.JsonResource;

import java.util.Map;

public class ConfiguredCarverBuilder extends TypedJsonBuilder<JsonResource<JsonObject>> {
	public ConfiguredCarverBuilder() {
		super(new JsonObject(), JsonResource::new);
	}

	/**
	 * @param id ID of an existing carver.
	 * @return this
	 */
	public ConfiguredCarverBuilder type(String id) {
		this.root.addProperty("type", id);
		return this;
	}

	public ConfiguredCarverBuilder y(HeightProviderBuilders processor) {
		with(this.root.getAsJsonObject("config"), "y", JsonObject::new, processor::buildTo);
		return this;
	}

	public ConfiguredCarverBuilder yScale(HeightProviderBuilders processor) {
		with(this.root.getAsJsonObject("config"), "yScale", JsonObject::new, processor::buildTo);
		return this;
	}

	public ConfiguredCarverBuilder lavaLevel(Map.Entry<String, Integer> obj) {
		with(this.root.getAsJsonObject("config"), "lava_level", JsonObject::new, jsonObject -> jsonObject.addProperty(obj.getKey(), obj.getValue()));
		return this;
	}

	public ConfiguredCarverBuilder horizontalRadiusModifier(HeightProviderBuilders processor) {
		with(this.root.getAsJsonObject("config"), "horizontal_radius_multiplier", JsonObject::new, processor::buildTo);
		return this;
	}

	public ConfiguredCarverBuilder verticalRadiusModifier(HeightProviderBuilders processor) {
		with(this.root.getAsJsonObject("config"), "vertical_radius_multiplier", JsonObject::new, processor::buildTo);
		return this;
	}

	public ConfiguredCarverBuilder floorLevel(HeightProviderBuilders processor) {
		with(this.root.getAsJsonObject("config"), "floor_level", JsonObject::new, processor::buildTo);
		return this;
	}

	public ConfiguredCarverBuilder probability(float probability) {
		try {
			if (probability > 1.0F) throw new Throwable("Probability can't be higher than 1.0F! Found " + probability);
			if (probability < 0.0F) throw new Throwable("Probability can't be smaller than 0.0F! Found " + probability);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("probability", probability);
			this.root.add("config", jsonObject);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return this;
	}


}
