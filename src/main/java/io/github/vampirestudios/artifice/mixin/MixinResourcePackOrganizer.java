package io.github.vampirestudios.artifice.mixin;

import io.github.vampirestudios.artifice.api.virtualpack.ArtificeResourcePackContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.resource.pack.ResourcePackProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.stream.Stream;

@Mixin(ResourcePackOrganizer.class)
@Environment(EnvType.CLIENT)
public class MixinResourcePackOrganizer {

	@Redirect(method = "getDisabledPacks", at = @At(value = "INVOKE", target = "Ljava/util/List;stream()Ljava/util/stream/Stream;"))
	private Stream<ResourcePackProfile> hideNoDisplayPacksFromDisabled(List<ResourcePackProfile> list) {
		return list.stream().filter(this::isVisible);
	}

	@Redirect(method = "getEnabledPacks", at = @At(value = "INVOKE", target = "Ljava/util/List;stream()Ljava/util/stream/Stream;"))
	private Stream<ResourcePackProfile> hideNoDisplayPacksFromEnabled(List<ResourcePackProfile> list) {
		return list.stream().filter(this::isVisible);
	}

	private boolean isVisible(ResourcePackProfile profile) {
		return !(profile instanceof ArtificeResourcePackContainer)
										|| ((ArtificeResourcePackContainer) profile).isVisible();
	}
}


