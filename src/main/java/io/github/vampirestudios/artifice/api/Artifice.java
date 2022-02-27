package io.github.vampirestudios.artifice.api;

import io.github.vampirestudios.artifice.api.ArtificeResourcePack.ClientResourcePackBuilder;
import io.github.vampirestudios.artifice.api.ArtificeResourcePack.ServerResourcePackBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;
import io.github.vampirestudios.artifice.common.ArtificeRegistry;
import io.github.vampirestudios.artifice.impl.ArtificeImpl;
import io.github.vampirestudios.artifice.impl.DynamicResourcePackFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

/**
 * Registry methods for Artifice's virtual resource pack support.
 */
public final class Artifice {
    private Artifice() {
    }

    /**
     * @deprecated Deprecated in favor of {@link Artifice#registerAssetPack(String, Processor)}
     */
    @Deprecated
    @Environment(EnvType.CLIENT)
    public static ArtificeResourcePack registerAssets(String id, Processor<ClientResourcePackBuilder> register) {
        return registerAssets(new ResourceLocation(id), register);
    }

    /**
     * @deprecated Deprecated in favor of {@link Artifice#registerDataPack(String, Processor)}
     */
    @Deprecated
    public static ArtificeResourcePack registerData(String id, Processor<ServerResourcePackBuilder> register) {
        return registerData(new ResourceLocation(id), register);
    }

    /**
     * @deprecated Deprecated in favor of {@link Artifice#registerAssetPack(ResourceLocation, Processor)}
     */
    @Deprecated
    @Environment(EnvType.CLIENT)
    public static ArtificeResourcePack registerAssets(ResourceLocation id, Processor<ClientResourcePackBuilder> register) {
        ArtificeImpl.LOGGER.warn("Using deprecated Artifice#registerAssets! Please use registerAssetPack! Issues may occur!");
        return ArtificeImpl.registerSafely(ArtificeRegistry.ASSETS, id, ArtificeResourcePack.ofAssets(register));
    }

    /**
     * @deprecated Deprecated in favor of {@link Artifice#registerDataPack(ResourceLocation, Processor)}
     */
    @Deprecated
    public static ArtificeResourcePack registerData(ResourceLocation id, Processor<ServerResourcePackBuilder> register) {
        ArtificeImpl.LOGGER.warn("Using deprecated Artifice#registerData! Please use registerDataPack! Issues may occur!");
        return ArtificeImpl.registerSafely(ArtificeRegistry.DATA, id, ArtificeResourcePack.ofData(register));
    }

    /**
     * Register a new client-side resource pack, creating resources with the given callback.
     *
     * @param id       The pack ID.
     * @param register A callback which will be passed a {@link ClientResourcePackBuilder}.
     * @return The registered pack.
     */
    @Environment(EnvType.CLIENT)
    public static void registerAssetPack(String id, Processor<ClientResourcePackBuilder> register) {
        registerAssetPack(new ResourceLocation(id), register);
    }

    /**
     * Register a new server-side resource pack, creating resources with the given callback.
     *
     * @param id       The pack ID.
     * @param register A callback which will be passed a {@link ServerResourcePackBuilder}.
     * @return The registered pack.
     */
    public static void registerDataPack(String id, Processor<ServerResourcePackBuilder> register) {
        registerDataPack(new ResourceLocation(id), register);
    }

    /**
     * Register a new client-side resource pack, creating resources with the given callback.
     *
     * @param id       The pack ID.
     * @param register A callback which will be passed a {@link ClientResourcePackBuilder}.
     * @return The registered pack.
     */
    @Environment(EnvType.CLIENT)
    public static void registerAssetPack(ResourceLocation id, Processor<ClientResourcePackBuilder> register) {
        ArtificeImpl.registerSafely(ArtificeRegistry.ASSETS, id, new DynamicResourcePackFactory<>(PackType.CLIENT_RESOURCES, id, register));
    }


    /**
     * Register a new server-side resource pack, creating resources with the given callback.
     *
     * @param id       The pack ID.
     * @param register A callback which will be passed a {@link ServerResourcePackBuilder}.
     * @return The registered pack.
     */
    public static void registerDataPack(ResourceLocation id, Processor<ServerResourcePackBuilder> register) {
        ArtificeImpl.registerSafely(ArtificeRegistry.DATA, id, new DynamicResourcePackFactory<>(PackType.SERVER_DATA, id, register));
    }

    /**
     * Register a new client-side resource pack.
     *
     * @param id   The pack ID.
     * @param pack The pack to register.
     * @return The registered pack.
     */
    @Environment(EnvType.CLIENT)
    public static ArtificeResourcePack registerAssets(String id, ArtificeResourcePack pack) {
        return registerAssets(new ResourceLocation(id), pack);
    }

    /**
     * Register a new server-side resource pack.
     *
     * @param id   The pack ID.
     * @param pack The pack to register.
     * @return The registered pack.
     */
    public static ArtificeResourcePack registerData(String id, ArtificeResourcePack pack) {
        return registerData(new ResourceLocation(id), pack);
    }

    /**
     * Register a new client-side resource pack.
     *
     * @param id   The pack ID.
     * @param pack The pack to register.
     * @return The registered pack.
     * @see ArtificeResourcePack#ofAssets
     */
    @Environment(EnvType.CLIENT)
    public static ArtificeResourcePack registerAssets(ResourceLocation id, ArtificeResourcePack pack) {
        if (pack.getType() != PackType.CLIENT_RESOURCES)
            throw new IllegalArgumentException("Cannot register a server-side pack as assets");
        return ArtificeImpl.registerSafely(ArtificeRegistry.ASSETS, id, pack);
    }

    /**
     * Register a new server-side resource pack.
     *
     * @param id   The pack ID.
     * @param pack The pack to register.
     * @return The registered pack.
     * @see ArtificeResourcePack#ofData
     */
    public static ArtificeResourcePack registerData(ResourceLocation id, ArtificeResourcePack pack) {
        if (pack.getType() != PackType.SERVER_DATA)
            throw new IllegalArgumentException("Cannot register a client-side pack as data");
        return ArtificeImpl.registerSafely(ArtificeRegistry.DATA, id, pack);
    }
}
