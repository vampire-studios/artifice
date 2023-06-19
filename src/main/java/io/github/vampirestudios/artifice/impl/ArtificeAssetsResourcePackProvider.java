package io.github.vampirestudios.artifice.impl;

import io.github.vampirestudios.artifice.common.ArtificeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.RepositorySource;

import java.util.function.Consumer;

public final class ArtificeAssetsResourcePackProvider implements RepositorySource {

	@Override
	public void loadPacks(Consumer<Pack> profileAdder) {
		for (ResourceLocation id : ArtificeRegistry.ASSETS.keySet()) {
			profileAdder.accept(ArtificeRegistry.ASSETS.get(id).toClientResourcePackProfile().get());
		}
	}
}

