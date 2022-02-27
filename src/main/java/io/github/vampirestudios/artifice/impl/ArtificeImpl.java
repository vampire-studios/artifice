package io.github.vampirestudios.artifice.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class ArtificeImpl {

    private static final Logger log4jLogger = LogManager.getLogger("Artifice");
    public static final Logger LOGGER = log4jLogger;

    public static <V, T extends V> T registerSafely(Registry<V> registry, ResourceLocation id, T entry) {
        return Registry.register(registry, id, entry);
    }
}
