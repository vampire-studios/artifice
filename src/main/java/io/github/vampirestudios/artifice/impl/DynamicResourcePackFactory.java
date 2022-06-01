package io.github.vampirestudios.artifice.impl;

import io.github.vampirestudios.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.artifice.common.ClientOnly;
import io.github.vampirestudios.artifice.common.ClientResourcePackProfileLike;
import io.github.vampirestudios.artifice.common.ServerResourcePackProfileLike;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;

import java.util.function.Consumer;

public class DynamicResourcePackFactory<T extends ArtificeResourcePack.ResourcePackBuilder> implements ClientResourcePackProfileLike, ServerResourcePackProfileLike {

    private final PackType type;
    private final ResourceLocation identifier;
    private final Consumer<T> init;

    public DynamicResourcePackFactory(PackType type, ResourceLocation identifier, Consumer<T> init) {
        this.type = type;
        this.identifier = identifier;
        this.init = init;
    }

    @Override
    public ClientOnly<Pack> toClientResourcePackProfile(Pack.PackConstructor factory) {
        return new ArtificeResourcePackImpl(type, identifier, init).toClientResourcePackProfile(factory);
    }

    @Override
    public Pack toServerResourcePackProfile(Pack.PackConstructor factory) {
        return new ArtificeResourcePackImpl(type, identifier, init).toServerResourcePackProfile(factory);
    }
}
