package io.github.vampirestudios.artifice.mixin;

import io.github.vampirestudios.artifice.api.virtualpack.ArtificeResourcePackContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public abstract class MixinMinecraftClient {

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/repository/PackRepository;reload()V"))
	private void enableNonOptional(PackRepository resourcePackManager) {
		Collection<Pack> enabled = resourcePackManager.getSelectedPacks();
		for (Pack profile : resourcePackManager.getAvailablePacks()) {
			if (profile instanceof ArtificeResourcePackContainer && !((ArtificeResourcePackContainer) profile).isOptional()) {
				if (!enabled.contains(profile)) enabled.add(profile);
			}
		}

		resourcePackManager.reload();
	}
}
