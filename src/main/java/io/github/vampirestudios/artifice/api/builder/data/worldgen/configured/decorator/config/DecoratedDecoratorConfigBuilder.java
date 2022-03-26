package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.decorator.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.decorator.ConfiguredDecoratorBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class DecoratedDecoratorConfigBuilder extends DecoratorConfigBuilder {

    public DecoratedDecoratorConfigBuilder() {
        super();
    }

    public DecoratedDecoratorConfigBuilder outerDecorator(ConfiguredDecoratorBuilder processor) {
        join("outer", processor.getData());
        return this;
    }

    public DecoratedDecoratorConfigBuilder innerDecorator(ConfiguredDecoratorBuilder processor) {
        join("inner", processor.getData());
        return this;
    }
}
