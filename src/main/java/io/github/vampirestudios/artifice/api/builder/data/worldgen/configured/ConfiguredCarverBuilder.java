package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.FloatProviderBuilders;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.HeightProviderBuilders;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.YOffsetBuilder;

public class ConfiguredCarverBuilder extends TypedJsonObject {
	public ConfiguredCarverBuilder() {
		super(new JsonObject());
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
		join(this.root.getAsJsonObject("config"), "y", processor.build());
		return this;
	}

	public ConfiguredCarverBuilder yScale(FloatProviderBuilders processor) {
		join(this.root.getAsJsonObject("config"), "yScale", processor.build());
		return this;
	}

	public ConfiguredCarverBuilder lavaLevel(YOffsetBuilder obj) {
		join(this.root.getAsJsonObject("config"), "lava_level", obj.build());
		return this;
	}

	public ConfiguredCarverBuilder horizontalRadiusModifier(FloatProviderBuilders processor) {
		join(this.root.getAsJsonObject("config"), "horizontal_radius_multiplier", processor.build());
		return this;
	}

	public ConfiguredCarverBuilder verticalRadiusModifier(FloatProviderBuilders processor) {
		join(this.root.getAsJsonObject("config"), "vertical_radius_multiplier", processor.build());
		return this;
	}

	public ConfiguredCarverBuilder floorLevel(FloatProviderBuilders processor) {
		join(this.root.getAsJsonObject("config"), "floor_level", processor.build());
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
