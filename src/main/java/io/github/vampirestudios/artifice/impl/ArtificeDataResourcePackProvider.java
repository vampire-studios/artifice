package io.github.vampirestudios.artifice.impl;

import io.github.vampirestudios.artifice.common.ArtificeRegistry;
import net.minecraft.resource.pack.ResourcePackProfile;
import net.minecraft.resource.pack.ResourcePackProvider;
import net.minecraft.util.Identifier;

import java.util.Objects;
import java.util.function.Consumer;

public final class ArtificeDataResourcePackProvider implements ResourcePackProvider {
    @Override
    public void register(Consumer<ResourcePackProfile> consumer, ResourcePackProfile.Factory factory) {
        for (Identifier id : ArtificeRegistry.DATA.getIds()) {
            consumer.accept(Objects.requireNonNull(ArtificeRegistry.DATA.get(id)).toServerResourcePackProfile(factory));
        }
    }
}
