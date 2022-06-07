package com.swordglowsblue.artifice.test;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class RegistryUtils<T> {
	private final Registry<T> registry;

	public RegistryUtils(Registry<T> registry) {
		this.registry = registry;
	}

	public ResourceLocation getIdByType(T type) {
		return registry.getKey(type);
	}

}
