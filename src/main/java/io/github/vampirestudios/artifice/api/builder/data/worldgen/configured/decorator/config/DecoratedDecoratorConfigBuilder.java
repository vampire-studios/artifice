package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.decorator.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.decorator.ConfiguredDecoratorBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class DecoratedDecoratorConfigBuilder extends DecoratorConfigBuilder {

    public DecoratedDecoratorConfigBuilder() {
        super();
    }

    public DecoratedDecoratorConfigBuilder outerDecorator(Processor<ConfiguredDecoratorBuilder> processor) {
        with("outer", JsonObject::new, jsonObject -> processor.process(new ConfiguredDecoratorBuilder()).buildTo(jsonObject));
        return this;
    }

    public DecoratedDecoratorConfigBuilder innerDecorator(Processor<ConfiguredDecoratorBuilder> processor) {
        with("inner", JsonObject::new, jsonObject -> processor.process(new ConfiguredDecoratorBuilder()).buildTo(jsonObject));
        return this;
    }
}
