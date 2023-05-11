package io.github.vampirestudios.artifice.mixin;

import io.github.vampirestudios.artifice.impl.ArtificeAssetsResourcePackProvider;
import io.github.vampirestudios.artifice.impl.ArtificeDataResourcePackProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(PackRepository.class)
public class MixinResourcePackManager {
	private static final ArtificeAssetsResourcePackProvider clientProvider = new ArtificeAssetsResourcePackProvider();
	private static final ArtificeDataResourcePackProvider serverProvider = new ArtificeDataResourcePackProvider();

	/*@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableSet;copyOf([Ljava/lang/Object;)Lcom/google/common/collect/ImmutableSet;"))
	private <E> ImmutableSet<Object> appendArtificePacks(E[] elements) {
		Object addedPack;
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			addedPack = serverProvider;
		} else {
			boolean isForClient = Arrays.stream(elements).anyMatch(ClientPackSource.class::isInstance);
			addedPack = isForClient ? clientProvider : serverProvider;
		}
		return ImmutableSet.copyOf(ArrayUtils.add(elements, addedPack));
	}*/

	@Shadow
	@Final
	@Mutable
	private Set<RepositorySource> sources;

	/**
	 * Injects our custom resource pack provider.
	 */
	@Inject(method = "<init>", at = @At(value = "RETURN"))
	private <E> void injectSPResourcePack(RepositorySource[] resourcePackProviders, CallbackInfo ci) {
		sources = new HashSet<>(sources);

		boolean isClient = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
		boolean providerPresent = false;

		for (RepositorySource element : sources) {
			if (element instanceof ArtificeAssetsResourcePackProvider) {
				isClient = true;
				providerPresent = true;
				break;
			}

			if (element instanceof ArtificeDataResourcePackProvider) {
				providerPresent = true;
				break;
			}
		}

		if (!providerPresent) {
			if (isClient) {
				//System.out.println("### adding client");
				sources.add(new ArtificeAssetsResourcePackProvider());
			} else {
				//System.out.println("### adding server");
				sources.add(new ArtificeDataResourcePackProvider());
			}
		}
	}

	@Inject(method = "reload", at = @At(value = "TAIL"))
	private void addSubResourcePacks(CallbackInfo ci) {
		//System.out.println("### server? " + (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER));
		//System.out.println("### providers: " + this.providers);
		//System.out.println("### profiles: " + this.profiles);
		//System.out.println("### packs: " + this.enabled);

		/*for (RepositorySource provider : this.sources) {
			if (provider instanceof ArtificeAssetsResourcePackProvider) {
				//System.out.println("### client present");
				((ArtificeAssetsResourcePackProvider) provider).add();
			}

			if (provider instanceof SPServerResourcePackProvider) {
				//System.out.println("### server present");
				((SPServerResourcePackProvider) provider).addSubResourcePacks();
			}
		}*/
	}
}
