package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.decorator.config;

public class CountExtraDecoratorConfigBuilder extends DecoratorConfigBuilder {

	public CountExtraDecoratorConfigBuilder() {
		super();
	}

    public CountExtraDecoratorConfigBuilder count(int count) {
        add("count", count);
        return this;
    }

    public CountExtraDecoratorConfigBuilder extraCount(int extraCount) {
        add("extra_count", extraCount);
        return this;
    }

    public CountExtraDecoratorConfigBuilder extraChance(float extraChance) {
        add("extra_chance", extraChance);
        return this;
    }
}
