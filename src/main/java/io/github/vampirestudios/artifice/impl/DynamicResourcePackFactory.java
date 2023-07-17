package io.github.vampirestudios.artifice.impl;

import io.github.vampirestudios.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.artifice.common.ClientOnly;
import io.github.vampirestudios.artifice.common.ClientResourcePackProfileLike;
import io.github.vampirestudios.artifice.common.ServerResourcePackProfileLike;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceType;

import java.util.function.Consumer;

public class DynamicResourcePackFactory<T extends ArtificeResourcePack.ResourcePackBuilder> implements ClientResourcePackProfileLike, ServerResourcePackProfileLike {

	private final ResourceType type;
	private final Identifier identifier;
	private final Consumer<T> init;

	public DynamicResourcePackFactory(ResourceType type, Identifier identifier, Consumer<T> init) {
		this.type = type;
		this.identifier = identifier;
		this.init = init;
	}

	@Override
	public ClientOnly<Pack> toClientResourcePackProfile() {
		return new ArtificeResourcePackImpl(type, identifier, init).toClientResourcePackProfile();
	}

	@Override
	public Pack toServerResourcePackProfile() {
		return new ArtificeResourcePackImpl(type, identifier, init).toServerResourcePackProfile();
	}
}
