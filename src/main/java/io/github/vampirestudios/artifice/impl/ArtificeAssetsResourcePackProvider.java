package io.github.vampirestudios.artifice.impl;

import io.github.vampirestudios.artifice.common.ArtificeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.RepositorySource;

import java.util.Objects;
import java.util.function.Consumer;

public final class ArtificeAssetsResourcePackProvider implements RepositorySource {

	@Environment(EnvType.CLIENT)
	@Override
	public void loadPacks(Consumer<Pack> consumer, Pack.PackConstructor factory) {
		for (ResourceLocation id : ArtificeRegistry.ASSETS.keySet()) {
			consumer.accept(Objects.requireNonNull(ArtificeRegistry.ASSETS.get(id)).toClientResourcePackProfile(factory).get());
		}
	}
}

