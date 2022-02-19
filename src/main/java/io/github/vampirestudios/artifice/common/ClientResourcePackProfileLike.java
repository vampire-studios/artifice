package io.github.vampirestudios.artifice.common;

import net.minecraft.resource.pack.ResourcePackProfile;

public interface ClientResourcePackProfileLike {
    // Supplier to avoid loading ClientResourcePackProfile on the server
    <T extends ResourcePackProfile> ClientOnly<ResourcePackProfile> toClientResourcePackProfile(ResourcePackProfile.Factory factory);
}
