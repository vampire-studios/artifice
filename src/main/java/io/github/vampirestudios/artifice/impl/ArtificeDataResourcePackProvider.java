package io.github.vampirestudios.artifice.impl;

import io.github.vampirestudios.artifice.common.ArtificeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.RepositorySource;

import java.util.function.Consumer;

public final class ArtificeDataResourcePackProvider implements RepositorySource {
	@Override
	public void loadPacks(Consumer<Pack> consumer) {
		for (ResourceLocation id : ArtificeRegistry.DATA.keySet()) {
			consumer.accept(ArtificeRegistry.DATA.get(id).toServerResourcePackProfile());
		}
	}
}
