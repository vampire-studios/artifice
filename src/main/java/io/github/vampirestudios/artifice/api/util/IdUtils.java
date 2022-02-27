package io.github.vampirestudios.artifice.api.util;

import net.minecraft.resources.ResourceLocation;

/** Utilities for modifying {@link ResourceLocation}s. */
public final class IdUtils {
    /**
     * Add a prefix to the path of the given {@link ResourceLocation}.
     * @param prefix The prefix to add.
     * @param id The base ID.
     * @return A new {@link ResourceLocation} with the prefixed path.
     */
    public static ResourceLocation wrapPath(String prefix, ResourceLocation id) { return wrapPath(prefix, id, ""); }

    /**
     * Add a suffix to the path of the given {@link ResourceLocation}.
     * @param id The base ID.
     * @param suffix The suffix to add.
     * @return A new {@link ResourceLocation} with the suffixed path.
     */
    public static ResourceLocation wrapPath(ResourceLocation id, String suffix) { return wrapPath("", id, suffix); }

    /**
     * Add a prefix and suffix to the path of the given {@link ResourceLocation}.
     * @param prefix The prefix to add.
     * @param id The base ID.
     * @param suffix The suffix to add.
     * @return A new {@link ResourceLocation} with the wrapped path.
     */
    public static ResourceLocation wrapPath(String prefix, ResourceLocation id, String suffix) {
        if(prefix.isEmpty() && suffix.isEmpty()) return id;
        return new ResourceLocation(id.getNamespace(), prefix+id.getPath()+suffix);
    }

    /**
     * If the given {@link ResourceLocation} has the namespace "minecraft" (the default namespace),
     *   return a copy with the given {@code defaultNamespace}. Otherwise, return the ID unchanged.
     * @param id The base ID.
     * @param defaultNamespace The namespace to replace {@code minecraft} with if applicable.
     * @return The given ID with its namespace replaced if applicable.
     */
    public static ResourceLocation withDefaultNamespace(ResourceLocation id, String defaultNamespace) {
        return id.getNamespace().equals("minecraft") ? new ResourceLocation(defaultNamespace, id.getPath()) : id;
    }
}
