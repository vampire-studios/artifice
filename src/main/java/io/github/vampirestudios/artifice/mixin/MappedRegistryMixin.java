package io.github.vampirestudios.artifice.mixin;

import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MappedRegistry.class)
public class MappedRegistryMixin<T> {
	@Inject(method = "freeze", at = @At("HEAD"), cancellable = true)
	private void disableThatThing(CallbackInfoReturnable<Registry<T>> cir) {
		cir.setReturnValue((Registry<T>) (Object)this);
	}
}
