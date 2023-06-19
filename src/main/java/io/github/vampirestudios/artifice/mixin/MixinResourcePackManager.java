package io.github.vampirestudios.artifice.mixin;

import com.google.common.collect.ImmutableSet;
import io.github.vampirestudios.artifice.impl.ArtificeAssetsResourcePackProvider;
import io.github.vampirestudios.artifice.impl.ArtificeDataResourcePackProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.resources.ClientPackSource;
import net.minecraft.server.packs.repository.PackRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Arrays;

@Mixin(PackRepository.class)
public class MixinResourcePackManager {
	private static final ArtificeAssetsResourcePackProvider clientProvider = new ArtificeAssetsResourcePackProvider();
	private static final ArtificeDataResourcePackProvider serverProvider = new ArtificeDataResourcePackProvider();

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableSet;copyOf([Ljava/lang/Object;)Lcom/google/common/collect/ImmutableSet;"))
	private <E> ImmutableSet<Object> appendArtificePacks(E[] elements) {
		Object addedPack;
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			addedPack = serverProvider;
		} else {
			boolean isForClient = Arrays.stream(elements).anyMatch(ClientPackSource.class::isInstance);
			addedPack = isForClient ? clientProvider : serverProvider;
		}
		return ImmutableSet.copyOf(ArrayUtils.add(elements, addedPack));
	}
}
