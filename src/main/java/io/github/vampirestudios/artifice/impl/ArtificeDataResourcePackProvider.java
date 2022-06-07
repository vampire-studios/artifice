package io.github.vampirestudios.artifice.impl;

import io.github.vampirestudios.artifice.common.ArtificeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.RepositorySource;

import java.util.Objects;
import java.util.function.Consumer;

public final class ArtificeDataResourcePackProvider implements RepositorySource {
	@Override
	public void loadPacks(Consumer<Pack> consumer, Pack.PackConstructor factory) {
		for (ResourceLocation id : ArtificeRegistry.DATA.keySet()) {
			consumer.accept(Objects.requireNonNull(ArtificeRegistry.DATA.get(id)).toServerResourcePackProfile(factory));
		}
	}
}
