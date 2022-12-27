package io.github.vampirestudios.artifice.mixin;

import io.github.vampirestudios.artifice.common.ArtificeRegistry;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(MultiPackResourceManager.class)
public abstract class LifecycledResourceManagerImplMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
	private static List<PackResources> registerARRPs(List<PackResources> packs, PackType type, List<PackResources> packs0) {
		List<PackResources> copy = new ArrayList<>(packs);
		if (type.equals(PackType.CLIENT_RESOURCES)) ArtificeRegistry.ASSETS.forEach(clientResourcePackProfileLike -> copy.add(clientResourcePackProfileLike.toClientResourcePackProfile().get().open()));
		if (type.equals(PackType.SERVER_DATA)) ArtificeRegistry.DATA.forEach(serverResourcePackProfileLike -> copy.add(serverResourcePackProfileLike.toServerResourcePackProfile().open()));
		return copy;
	}
}