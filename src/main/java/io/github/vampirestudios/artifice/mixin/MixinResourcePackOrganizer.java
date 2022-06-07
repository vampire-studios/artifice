package io.github.vampirestudios.artifice.mixin;

import io.github.vampirestudios.artifice.api.virtualpack.ArtificeResourcePackContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.stream.Stream;

@Mixin(PackSelectionModel.class)
@Environment(EnvType.CLIENT)
public class MixinResourcePackOrganizer {

	@Redirect(method = "getUnselected", at = @At(value = "INVOKE", target = "Ljava/util/List;stream()Ljava/util/stream/Stream;"))
	private Stream<Pack> hideNoDisplayPacksFromDisabled(List<Pack> list) {
		return list.stream().filter(this::isVisible);
	}

	@Redirect(method = "getSelected", at = @At(value = "INVOKE", target = "Ljava/util/List;stream()Ljava/util/stream/Stream;"))
	private Stream<Pack> hideNoDisplayPacksFromEnabled(List<Pack> list) {
		return list.stream().filter(this::isVisible);
	}

	private boolean isVisible(Pack profile) {
		return !(profile instanceof ArtificeResourcePackContainer)
				|| ((ArtificeResourcePackContainer) profile).isVisible();
	}
}


