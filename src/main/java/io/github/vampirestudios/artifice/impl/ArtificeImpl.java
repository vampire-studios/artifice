package io.github.vampirestudios.artifice.impl;

import io.github.vampirestudios.artifice.api.Artifice;
import io.github.vampirestudios.artifice.api.ArtificeAssetPackEntrypoint;
import io.github.vampirestudios.artifice.api.ArtificeDataPackEntrypoint;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class ArtificeImpl implements ModInitializer {

    @Override
    public void onInitialize() {
        for (ArtificeAssetPackEntrypoint entrypoint : FabricLoader.getInstance().getEntrypoints("artifice:asset_pack", ArtificeAssetPackEntrypoint.class)) {
            Artifice.registerAssetPack(entrypoint.getClientID(), entrypoint::generateAssetPack);
        }
        for (ArtificeDataPackEntrypoint entrypoint : FabricLoader.getInstance().getEntrypoints("artifice:data_pack", ArtificeDataPackEntrypoint.class)) {
            Artifice.registerDataPack(entrypoint.getServerID(), entrypoint::generateDataPack);
        }
    }

    public static <V, T extends V> T registerSafely(Registry<V> registry, ResourceLocation id, T entry) {
        return Registry.register(registry, id, entry);
    }

}