package io.github.vampirestudios.artifice.impl;

import io.github.vampirestudios.artifice.api.Artifice;
import io.github.vampirestudios.artifice.api.ArtificeClientResourcePackEntrypoint;
import io.github.vampirestudios.artifice.api.ArtificeServerResourcePackEntrypoint;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class ArtificeImpl implements ModInitializer {

    @Override
    public void onInitialize() {
        for (ArtificeClientResourcePackEntrypoint entrypoint : FabricLoader.getInstance().getEntrypoints("artifice:client_resource_pack", ArtificeClientResourcePackEntrypoint.class)) {
            Artifice.registerAssetPack(entrypoint.getClientID(), entrypoint::generateClientResourcePack);
        }

        for (ArtificeServerResourcePackEntrypoint entrypoint : FabricLoader.getInstance().getEntrypoints("artifice:server_resource_pack", ArtificeServerResourcePackEntrypoint.class)) {

        }
    }

    public static <V, T extends V> T registerSafely(Registry<V> registry, ResourceLocation id, T entry) {
        return Registry.register(registry, id, entry);
    }

}