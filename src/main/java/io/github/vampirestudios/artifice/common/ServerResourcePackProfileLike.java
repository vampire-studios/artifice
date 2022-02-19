package io.github.vampirestudios.artifice.common;

import net.minecraft.resource.pack.ResourcePackProfile;

public interface ServerResourcePackProfileLike {
    <T extends ResourcePackProfile> ResourcePackProfile toServerResourcePackProfile(ResourcePackProfile.Factory factory);
}
