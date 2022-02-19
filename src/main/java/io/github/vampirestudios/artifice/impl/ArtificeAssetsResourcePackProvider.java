package io.github.vampirestudios.artifice.impl;

import io.github.vampirestudios.artifice.common.ArtificeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.pack.ResourcePackProfile;
import net.minecraft.resource.pack.ResourcePackProvider;
import net.minecraft.util.Identifier;

import java.util.Objects;
import java.util.function.Consumer;

public final class ArtificeAssetsResourcePackProvider implements ResourcePackProvider {

    @Environment(EnvType.CLIENT)
    @Override
    public void register(Consumer<ResourcePackProfile> consumer, ResourcePackProfile.Factory factory) {
        for (Identifier id : ArtificeRegistry.ASSETS.getIds()) {
            consumer.accept(Objects.requireNonNull(ArtificeRegistry.ASSETS.get(id)).toClientResourcePackProfile(factory).get());
        }
    }
}

