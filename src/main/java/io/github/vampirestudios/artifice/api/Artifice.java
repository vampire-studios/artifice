package io.github.vampirestudios.artifice.api;

import com.mojang.logging.LogUtils;
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
import org.slf4j.Logger;

/**
 * Registry methods for Artifice's virtual resource pack support.
 */
public final class Artifice {
	public static final Logger LOGGER = LogUtils.getLogger();

	private Artifice() {
	}

	/**
	 * Register a new client-side resource pack, creating resources with the given callback.
	 *
	 * @param id       The pack ID.
	 * @param register A callback which will be passed a {@link ClientResourcePackBuilder}.
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
	 */
	public static ArtificeResourcePack registerData(ResourceLocation id, ArtificeResourcePack pack) {
		if (pack.getType() != PackType.SERVER_DATA)
			throw new IllegalArgumentException("Cannot register a client-side pack as data");
		return ArtificeImpl.registerSafely(ArtificeRegistry.DATA, id, pack);
	}
}
