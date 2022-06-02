package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.decorator.config;

import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.decorator.ConfiguredDecoratorBuilder;

public class DecoratedDecoratorConfigBuilder extends DecoratorConfigBuilder {

    public DecoratedDecoratorConfigBuilder() {
        super();
    }

    public DecoratedDecoratorConfigBuilder outerDecorator(ConfiguredDecoratorBuilder processor) {
        join("outer", processor.build());
        return this;
    }

    public DecoratedDecoratorConfigBuilder innerDecorator(ConfiguredDecoratorBuilder processor) {
        join("inner", processor.build());
        return this;
    }
}
