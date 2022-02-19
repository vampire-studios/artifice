package io.github.vampirestudios.artifice.impl;

import io.github.vampirestudios.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.artifice.common.ClientOnly;
import io.github.vampirestudios.artifice.common.ClientResourcePackProfileLike;
import io.github.vampirestudios.artifice.common.ServerResourcePackProfileLike;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.pack.ResourcePackProfile;
import net.minecraft.util.Identifier;

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
    public ClientOnly<ResourcePackProfile> toClientResourcePackProfile(ResourcePackProfile.Factory factory) {
        return new ArtificeResourcePackImpl(type, identifier, init).toClientResourcePackProfile(factory);
    }

    @Override
    public ResourcePackProfile toServerResourcePackProfile(ResourcePackProfile.Factory factory) {
        return new ArtificeResourcePackImpl(type, identifier, init).toServerResourcePackProfile(factory);
    }
}
