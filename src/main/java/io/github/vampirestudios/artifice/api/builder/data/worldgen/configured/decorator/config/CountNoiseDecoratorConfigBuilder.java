package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.decorator.config;

public class CountNoiseDecoratorConfigBuilder extends DecoratorConfigBuilder {

	public CountNoiseDecoratorConfigBuilder() {
		super();
	}

    public CountNoiseDecoratorConfigBuilder noiseLevel(double noiseLevel) {
        add("noise_level", noiseLevel);
        return this;
    }

    public CountNoiseDecoratorConfigBuilder belowNoise(int belowNoise) {
        add("below_noise", belowNoise);
        return this;
    }

    public CountNoiseDecoratorConfigBuilder aboveNoise(int aboveNoise) {
        add("above_noise", aboveNoise);
        return this;
    }
}
