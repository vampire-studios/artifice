package io.github.vampirestudios.artifice.impl;

import io.github.vampirestudios.artifice.api.Artifice;
import io.github.vampirestudios.artifice.api.ArtificeAssetPackEntrypoint;
import io.github.vampirestudios.artifice.api.ArtificeDataPackEntrypoint;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArtificeImpl implements ModInitializer {

    private static final Logger log4jLogger = LogManager.getLogger("Artifice");
    public static final Logger LOGGER = log4jLogger;

    public static <V, T extends V> T registerSafely(Registry<V> registry, ResourceLocation id, T entry) {
        return Registry.register(registry, id, entry);
    }

    @Override
    public void onInitialize() {
        for (ArtificeAssetPackEntrypoint entrypoint : FabricLoader.getInstance().getEntrypoints("artifice:asset_pack", ArtificeAssetPackEntrypoint.class)) {
            Artifice.registerAssetPack(entrypoint.getClientID(), entrypoint::generateClientResourcePack);
        }
        for (ArtificeDataPackEntrypoint entrypoint : FabricLoader.getInstance().getEntrypoints("artifice:data_pack", ArtificeDataPackEntrypoint.class)) {
            Artifice.registerDataPack(entrypoint.getServerID(), entrypoint::generateServerResourcePack);
        }
    }
}
