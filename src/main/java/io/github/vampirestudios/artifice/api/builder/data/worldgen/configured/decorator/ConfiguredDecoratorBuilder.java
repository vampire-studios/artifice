package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.decorator;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.decorator.config.DecoratorConfigBuilder;

public class ConfiguredDecoratorBuilder extends TypedJsonObject {

    public ConfiguredDecoratorBuilder() {
        super(new JsonObject());
    }

	public ConfiguredDecoratorBuilder name(String decoratorID) {
		this.root.addProperty("type", decoratorID);
		return this;
	}

    public <C extends DecoratorConfigBuilder> ConfiguredDecoratorBuilder config(C processor) {
        join("config", processor.build());
        return this;
    }

    public ConfiguredDecoratorBuilder defaultConfig() {
        return this.config(new DecoratorConfigBuilder());
    }
}
