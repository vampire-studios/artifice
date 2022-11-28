package io.github.vampirestudios.artifice.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.NativeImage;
import io.github.vampirestudios.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.artifice.api.builder.JsonObjectBuilder;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.builder.assets.*;
import io.github.vampirestudios.artifice.api.builder.data.AdvancementBuilder;
import io.github.vampirestudios.artifice.api.builder.data.LootTableBuilder;
import io.github.vampirestudios.artifice.api.builder.data.TagBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.DimensionBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.DimensionTypeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.recipe.*;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.NoiseSettingsBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.biome.BiomeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.ConfiguredCarverBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.ConfiguredFeatureBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.PlacedFeatureBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.structure.StructureBuilder;
import io.github.vampirestudios.artifice.api.resource.ArtificeResource;
import io.github.vampirestudios.artifice.api.resource.JsonResource;
import io.github.vampirestudios.artifice.api.util.CallableFunction;
import io.github.vampirestudios.artifice.api.util.CountingInputStream;
import io.github.vampirestudios.artifice.api.util.IdUtils;
import io.github.vampirestudios.artifice.api.util.Processor;
import io.github.vampirestudios.artifice.api.virtualpack.ArtificeResourcePackContainer;
import io.github.vampirestudios.artifice.common.ArtificeRegistry;
import io.github.vampirestudios.artifice.common.ClientOnly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.FileUtil;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.world.flag.FeatureFlagSet;
import org.apache.commons.io.input.NullInputStream;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.*;
import java.util.stream.Stream;

public class ArtificeResourcePackImpl implements ArtificeResourcePack {
	public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(Math.max(Runtime.getRuntime().availableProcessors() / 2 - 1, 1), new ThreadFactoryBuilder().setDaemon(true).setNameFormat("Artifice-Workers-%s").build());
	private final Lock waiting = new ReentrantLock();

	private final net.minecraft.server.packs.PackType type;
	@Nullable
	private final ResourceLocation identifier;
	private final Set<String> namespaces = new HashSet<>();
	private final Map<ResourceLocation, ArtificeResource<?>> resources = new HashMap<>();
	private final Set<LanguageInfo> languages = new HashSet<>();
	private final JsonResource<JsonObject> metadata;

	private Component description;
	private Component displayName;
	private boolean optional;
	private boolean visible;
	private boolean overwrite;

	@SuppressWarnings("unchecked")
	public <T extends ResourcePackBuilder> ArtificeResourcePackImpl(net.minecraft.server.packs.PackType type, @Nullable ResourceLocation identifier, Consumer<T> registerResources) {
		this.type = type;
		this.identifier = identifier;
		registerResources.accept((T) new ArtificeResourcePackBuilder());

		JsonObject packMeta;
		if (type.equals(net.minecraft.server.packs.PackType.CLIENT_RESOURCES)) {
			packMeta = new JsonObjectBuilder()
					.add("pack_format", SharedConstants.getCurrentVersion().getPackVersion(com.mojang.bridge.game.PackType.RESOURCE))
					.add("description", description != null ? description.getString() : "In-memory resource pack created with Artifice")
					.build();
		} else {
			packMeta = new JsonObjectBuilder()
					.add("pack_format", SharedConstants.getCurrentVersion().getPackVersion(com.mojang.bridge.game.PackType.DATA))
					.add("description", description != null ? description.getString() : "In-memory data pack created with Artifice")
					.build();
		}

		JsonObject languageMeta = new JsonObject();
		if (isClient()) {
			addLanguages(languageMeta);
		}

		JsonObjectBuilder builder = new JsonObjectBuilder();
		builder.add("pack", packMeta);
		if (languages.size() > 0) builder.add("language", languageMeta);
		this.metadata = new JsonResource<>(builder.build());
	}

	private boolean isClient() {
		try {
			return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
		} catch (NullPointerException e) {
			return true;
		}
	}

	@SuppressWarnings("MethodCallSideOnly")
	private void addLanguages(JsonObject languageMeta) {
		for (LanguageInfo def : languages) {
			languageMeta.add(def.getCode(), new JsonObjectBuilder()
					.add("name", def.getName())
					.add("region", def.getRegion())
					.add("bidirectional", def.isBidirectional())
					.build());
		}
	}

	@EnvironmentInterface(value = EnvType.CLIENT, itf = ClientResourcePackBuilder.class)
	private final class ArtificeResourcePackBuilder implements ClientResourcePackBuilder, ServerResourcePackBuilder {
		private final Map<ResourceLocation, Supplier<byte[]>> data = new ConcurrentHashMap<>();
		private final Map<ResourceLocation, Supplier<byte[]>> assets = new ConcurrentHashMap<>();
		private final Map<String, Supplier<byte[]>> root = new ConcurrentHashMap<>();
		private static final Logger LOGGER = LoggerFactory.getLogger(ArtificeResourcePackBuilder.class);
		private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();

		public ArtificeResourcePackBuilder() {
		}

		@Override
		public void setDisplayName(String name) {
			ArtificeResourcePackImpl.this.displayName = Component.literal(name);
		}

		@Override
		public void setDisplayName(Component title) {
			ArtificeResourcePackImpl.this.displayName = title;
		}

		@Override
		public void setDescription(String desc) {
			ArtificeResourcePackImpl.this.description = Component.literal(desc);
		}

		@Override
		public void setDescription(Component desc) {
			ArtificeResourcePackImpl.this.description = desc;
		}

		@Override
		@ApiStatus.Internal
		public void dumpResources(String filePath, String type, boolean enableDump) throws IOException {
			if (enableDump) {
				LOGGER.info("Dumping " + getName() + " " + type + " to " + filePath + ", this may take a while.");

				File dir = new File(filePath);
				if (!dir.exists() && !dir.mkdirs()) {
					throw new IOException("Can't dump resources to " + filePath + "; couldn't create parent directories");
				}
				if (!dir.isDirectory()) {
					throw new IllegalArgumentException("Can't dump resources to " + filePath + " as it's not a directory");
				}
				if (!dir.canWrite()) {
					throw new IOException("Can't dump resources to " + filePath + "; permission denied");
				}

				JsonObject packMeta;
				if (type.equals("assets")) {
					packMeta = new JsonObjectBuilder()
							.add("pack_format", SharedConstants.getCurrentVersion().getPackVersion(com.mojang.bridge.game.PackType.RESOURCE))
							.add("description", description != null ? description.getString() : "In-memory resource pack created with Artifice")
							.build();
				} else {
					packMeta = new JsonObjectBuilder()
							.add("pack_format", SharedConstants.getCurrentVersion().getPackVersion(com.mojang.bridge.game.PackType.DATA))
							.add("description", description != null ? description.getString() : "In-memory data pack created with Artifice")
							.build();
				}

				JsonObject languageMeta = new JsonObject();
				if (isClient()) {
					addLanguages(languageMeta);
				}

				JsonObjectBuilder builder = new JsonObjectBuilder();
				builder.add("pack", packMeta);
				if (languages.size() > 0) builder.add("language", languageMeta);
				JsonResource<JsonObject> mcmeta = new JsonResource<>(builder.build());
				new Thread(() -> {
					writeResourceFile(new File(filePath + "/pack.mcmeta"), mcmeta);
					try {
						File DEFAULT_OUTPUT = new File(filePath);
						Path folder = Paths.get(DEFAULT_OUTPUT.toURI());

						for (Map.Entry<String, Supplier<byte[]>> e : this.root.entrySet()) {
							Path root = folder.resolve(e.getKey());
							Files.createDirectories(root.getParent());
							Files.write(root, e.getValue().get());
						}

						Path assets = folder.resolve("assets");
						Files.createDirectories(assets);
						for (Map.Entry<ResourceLocation, Supplier<byte[]>> entry : this.assets.entrySet()) {
							this.write(assets, entry.getKey(), entry.getValue().get());
						}

						Path data = folder.resolve("data");
						Files.createDirectories(data);
						for (Map.Entry<ResourceLocation, Supplier<byte[]>> entry : this.data.entrySet()) {
							this.write(data, entry.getKey(), entry.getValue().get());
						}
					} catch (IOException exception) {
						throw new RuntimeException(exception);
					}
					resources.forEach((id, resource) -> {
						String path = String.format("./%s/%s/%s/%s", filePath, type, id.getNamespace(), id.getPath());
						writeResourceFile(new File(path), resource);
					});
					LOGGER.info("Finished dumping " + getName() + " " + type + ".");
				}).start();
			} else {
				LOGGER.info("Not dumping files cause dumping is disabled");
			}
		}

		@Override
		public void dump(String filePath, String type, boolean enableDump) {
			if (enableDump) {
				EXECUTOR.execute(() -> {
					try {
						dumpResources(filePath, type);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				});
			}
		}

		private void write(Path dir, ResourceLocation identifier, byte[] data) {
			try {
				Path file = dir.resolve(identifier.getNamespace()).resolve(identifier.getPath());
				Files.createDirectories(file.getParent());
				try (OutputStream output = Files.newOutputStream(file)) {
					output.write(data);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeResourceFile(File output, ArtificeResource<?> resource) {
			try {
				if (output.getParentFile().exists() || output.getParentFile().mkdirs()) {
					BufferedWriter writer = new BufferedWriter(new FileWriter(output));
					if (resource.getData() instanceof JsonElement) {
						Gson gson = new GsonBuilder().setPrettyPrinting().create();
						writer.write(gson.toJson(resource.getData()));
					} else {
						writer.write(resource.getData().toString());
					}
					writer.close();
				} else {
					throw new IOException("Failed to dump resource file " + output.getPath() + "; couldn't create parent directories");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void setVisible() {
			ArtificeResourcePackImpl.this.visible = true;
		}

		@Override
		public Future<byte[]> addAsyncResource(PackType type, ResourceLocation identifier, CallableFunction<ResourceLocation, byte[]> data) {
			Future<byte[]> future = EXECUTOR_SERVICE.submit(() -> data.get(identifier));
			this.getSys(type).put(identifier, () -> {
				try {
					return future.get();
				} catch (InterruptedException | ExecutionException e) {
					throw new RuntimeException(e);
				}
			});
			return future;
		}

		@Override
		public void addLazyResource(PackType type, ResourceLocation path, BiFunction<ArtificeResourcePack, ResourceLocation, byte[]> func) {
			this.getSys(type).put(path, new Memoized<>(func, path));
		}

		@Override
		public byte[] addResource(PackType type, ResourceLocation path, byte[] data) {
			this.getSys(type).put(path, () -> data);
			return data;
		}

		@Override
		public Future<byte[]> addAsyncRootResource(String path, CallableFunction<String, byte[]> data) {
			Future<byte[]> future = EXECUTOR_SERVICE.submit(() -> data.get(path));
			this.root.put(path, () -> {
				try {
					return future.get();
				} catch (InterruptedException | ExecutionException e) {
					throw new RuntimeException(e);
				}
			});
			return future;
		}

		@Override
		public void addLazyRootResource(String path, BiFunction<ArtificeResourcePack, String, byte[]> data) {
			this.root.put(path, new Memoized<>(data, path));
		}

		@Override
		public byte[] addRootResource(String path, byte[] data) {
			this.root.put(path, () -> data);
			return data;
		}

		@Override
		public byte[] addAsset(ResourceLocation path, byte[] data) {
			return this.addResource(PackType.CLIENT_RESOURCES, path, data);
		}

		@Override
		public byte[] addData(ResourceLocation path, byte[] data) {
			return this.addResource(PackType.SERVER_DATA, path, data);
		}

		@Override
		public void setOptional() {
			ArtificeResourcePackImpl.this.optional = true;
			ArtificeResourcePackImpl.this.visible = true;
		}

		@Override
		public void shouldOverwrite() {
			ArtificeResourcePackImpl.this.optional = false;
			ArtificeResourcePackImpl.this.visible = false;
			ArtificeResourcePackImpl.this.overwrite = true;
		}

		@Override
		public void add(ResourceLocation id, ArtificeResource<?> resource) {
			ArtificeResourcePackImpl.this.resources.put(id, resource);
			ArtificeResourcePackImpl.this.namespaces.add(id.getNamespace());
		}

		@Override
		public void addItemModel(ResourceLocation id, ModelBuilder f) {
			this.add("models/item/", id, ".json", f);
		}

		@Override
		public void addBlockModel(ResourceLocation id, ModelBuilder f) {
			this.add("models/block/", id, ".json", f);
		}

		@Override
		public void addBlockState(ResourceLocation id, BlockStateBuilder f) {
			this.add("blockstates/", id, ".json", f);
		}

		@Override
		public void addTranslations(ResourceLocation id, TranslationBuilder f) {
			this.add("lang/", id, ".json", f);
		}

		@Override
		public void addParticle(ResourceLocation id, ParticleBuilder f) {
			this.add("particles/", id, ".json", f);
		}

		@Override
		public void addItemAnimation(ResourceLocation id, AnimationBuilder f) {
			this.add("textures/item/", id, ".mcmeta", f);
		}

		@Override
		public void addBlockAnimation(ResourceLocation id, AnimationBuilder f) {
			this.add("textures/block/", id, ".mcmeta", f);
		}

		@Override
		public void addLanguage(LanguageInfo def) {
			ArtificeResourcePackImpl.this.languages.add(def);
		}

		@Environment(EnvType.CLIENT)
		@Override
		public void addLanguage(String code, String region, String name, boolean rtl) {
			this.addLanguage(new LanguageInfo(code, region, name, rtl));
		}

		@Override
		public void addTexture(ResourceLocation id, NativeImage image) {
			try {
				this.addAsset(fix(id, "textures", "png"), image.asByteArray());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void addRecoloredImage(ResourceLocation id, InputStream target, IntUnaryOperator operator) {
			this.addLazyResource(PackType.CLIENT_RESOURCES, fix(id, "textures", "png"), (i, r) -> {
				try {
					// optimize buffer allocation, input and output image after recoloring should be roughly the same size
					CountingInputStream is = new CountingInputStream(target);
					// repaint image
					BufferedImage base = ImageIO.read(is);
					try (NativeImage recolored = new NativeImage(base.getWidth(), base.getHeight(), false)) {
						for (int y = 0; y < base.getHeight(); y++) {
							for (int x = 0; x < base.getWidth(); x++) {
								recolored.setPixelRGBA(x, y, operator.applyAsInt(base.getRGB(x, y)));
							}
						}
						return recolored.asByteArray();
					}
				} catch (Throwable e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			});
		}

		@Override
		public void addAdvancement(ResourceLocation id, AdvancementBuilder f) {
			this.add("advancements/", id, ".json", f);
		}

		@Override
		public void addDimensionType(ResourceLocation id, DimensionTypeBuilder f) {
			this.add("dimension_type/", id, ".json", f);
		}

		@Override
		public void addDimension(ResourceLocation id, DimensionBuilder f) {
			this.add("dimension/", id, ".json", f);
		}

		@Override
		public void addBiome(ResourceLocation id, BiomeBuilder f) {
			this.add("worldgen/biome/", id, ".json", f);
		}

		@Override
		public void addConfiguredCarver(ResourceLocation id, ConfiguredCarverBuilder f) {
			this.add("worldgen/configured_carver/", id, ".json", f);
		}

		@Override
		public void addStructure(ResourceLocation id, StructureBuilder f) {
			this.add("worldgen/structure/", id, ".json", f);
		}

		@Override
		public void addConfiguredFeature(ResourceLocation id, ConfiguredFeatureBuilder f) {
			this.add("worldgen/configured_feature/", id, ".json", f);
		}

		@Override
		public void addPlacedFeature(ResourceLocation id, PlacedFeatureBuilder f) {
			this.add("worldgen/placed_feature/", id, ".json", f);
		}

		@Override
		public void addNoiseSettingsBuilder(ResourceLocation id, NoiseSettingsBuilder f) {
			this.add("worldgen/noise_settings/", id, ".json", f);
		}

		@Override
		public void addLootTable(ResourceLocation id, LootTableBuilder f) {
			this.add("loot_tables/", id, ".json", f);
		}

		@Override
		public void addTag(String tagType, ResourceLocation id, TagBuilder f) {
			this.add("tags/" + tagType + "/", id, ".json", f);
		}

		@Override
		public void addGenericRecipe(ResourceLocation id, GenericRecipeBuilder f) {
			this.add("recipes/", id, ".json", f);
		}

		@Override
		public void addShapedRecipe(ResourceLocation id, ShapedRecipeBuilder f) {
			this.add("recipes/", id, ".json", f);
		}

		@Override
		public void addShapelessRecipe(ResourceLocation id, ShapelessRecipeBuilder f) {
			this.add("recipes/", id, ".json", f);
		}

		@Override
		public void addStonecuttingRecipe(ResourceLocation id, StonecuttingRecipeBuilder f) {
			this.add("recipes/", id, ".json", f);
		}

		@Override
		public void addSmeltingRecipe(ResourceLocation id, CookingRecipeBuilder f) {
			this.add("recipes/", id, ".json", r -> r.type(new ResourceLocation("smelting")), CookingRecipeBuilder::new);
		}

		@Override
		public void addBlastingRecipe(ResourceLocation id, CookingRecipeBuilder f) {
			this.add("recipes/", id, ".json", r -> r.type(new ResourceLocation("blasting")), CookingRecipeBuilder::new);
		}

		@Override
		public void addSmokingRecipe(ResourceLocation id, CookingRecipeBuilder f) {
			this.add("recipes/", id, ".json", r -> r.type(new ResourceLocation("smoking")), CookingRecipeBuilder::new);
		}

		@Override
		public void addCampfireRecipe(ResourceLocation id, CookingRecipeBuilder f) {
			this.add("recipes/", id, ".json", r -> r.type(new ResourceLocation("campfire_cooking")), CookingRecipeBuilder::new);
		}

		@Override
		public void addSmithingRecipe(ResourceLocation id, SmithingRecipeBuilder f) {
			this.add("recipes/", id, ".json", r -> r.type(new ResourceLocation("blasting")), SmithingRecipeBuilder::new);
		}

		private <T extends TypedJsonObject> void add(String path, ResourceLocation id, String ext, Processor<T> f, Supplier<T> ctor) {
			this.add(IdUtils.wrapPath(path, id, ext), new JsonResource<>(f.process(ctor.get()).build()));
		}

		private <T extends TypedJsonObject> void add(String path, ResourceLocation id, String ext, T ctor) {
			this.add(IdUtils.wrapPath(path, id, ext), new JsonResource<>(ctor.build()));
		}

		private static ResourceLocation fix(ResourceLocation identifier, String prefix, String append) {
			return new ResourceLocation(identifier.getNamespace(), prefix + '/' + identifier.getPath() + '.' + append);
		}

		private Map<ResourceLocation, Supplier<byte[]>> getSys(PackType side) {
			return side == PackType.CLIENT_RESOURCES ? this.assets : this.data;
		}
	}

	@Nullable
	@Override
	public IoSupplier<InputStream> getRootResource(String... path) {
		if (path[path.length - 1].equals("pack.mcmeta")) return metadata::toInputStream;
		return () -> new NullInputStream(0);
	}

	@Override
	@Nullable
	public IoSupplier<InputStream> getResource(PackType type, ResourceLocation id) {
		Path path = this.root.resolve(type.getDirectory()).resolve(id.getNamespace());
		return PathPackResources.getResource(id, path);
	}

	@Override
	public void listResources(net.minecraft.server.packs.PackType type, String namespace, String startingPath, ResourceOutput pathFilter) {
		/*if (type != this.type) return new HashSet<>();
		Set<ResourceLocation> keys = new HashSet<>(this.resources.keySet());
		keys.removeIf(id -> !id.getPath().startsWith(startingPath) || !pathFilter.test(id));
		return keys;*/
		FileUtil.decomposePath(startingPath).get().ifLeft((pathSegments) -> {
			Path path = this.root.resolve(type.getDirectory()).resolve(namespace);
			listPath(namespace, path, pathSegments, consumer);
		}).ifRight((result) -> {
			LOGGER.error("Invalid path {}: {}", startingPath, result.message());
		});
	}

	public static void listPath(String namespace, Path path, List<String> pathSegments, PackResources.ResourceOutput consumer) {
		Path path2 = FileUtil.resolvePath(path, pathSegments);

		try {
			Stream<Path> stream = Files.find(path2, Integer.MAX_VALUE, (p, attr) -> {
				return attr.isRegularFile();
			}, new FileVisitOption[0]);

			try {
				stream.forEach((childPath) -> {
					String string2 = PATH_JOINER.join(path.relativize(childPath));
					ResourceLocation resourceLocation = ResourceLocation.tryBuild(namespace, string2);
					if (resourceLocation == null) {
						Util.logAndPauseIfInIde(String.format(Locale.ROOT, "Invalid path in pack: %s:%s, ignoring", namespace, string2));
					} else {
						consumer.accept(resourceLocation, IoSupplier.create(childPath));
					}

				});
			} catch (Throwable var9) {
				if (stream != null) {
					try {
						stream.close();
					} catch (Throwable var8) {
						var9.addSuppressed(var8);
					}
				}

				throw var9;
			}

			if (stream != null) {
				stream.close();
			}
		} catch (NoSuchFileException var10) {
		} catch (IOException var11) {
			LOGGER.error("Failed to list path {}", path2, var11);
		}

	}

	/*@Override
	public boolean hasResource(net.minecraft.server.packs.PackType type, ResourceLocation id) {
		return type == this.type && this.resources.containsKey(id);
	}*/

	@Override
	public <T> T getMetadataSection(MetadataSectionSerializer<T> reader) {
		return metadata.getData().has(reader.getMetadataSectionName())
				? reader.fromJson(metadata.getData().getAsJsonObject(reader.getMetadataSectionName()))
				: null;
	}

	@Override
	public Set<String> getNamespaces(net.minecraft.server.packs.PackType type) {
		return new HashSet<>(this.namespaces);
	}

	@Override
	public net.minecraft.server.packs.PackType getType() {
		return this.type;
	}

	@Override
	public boolean isOptional() {
		return this.optional;
	}

	@Override
	public boolean isVisible() {
		return this.visible;
	}

	@Override
	public boolean isShouldOverwrite() {
		return this.overwrite;
	}

	@Override
	public void close() {
	}

	@Override
	public String packId() {
		if (displayName == null) {
			switch (this.type) {
				case CLIENT_RESOURCES -> {
					ResourceLocation aid = ArtificeRegistry.ASSETS.getKey(this);
					displayName = Component.literal(aid != null ? aid.toString() : "Generated Resources");
					return displayName.getString();
				}
				case SERVER_DATA -> {
					ResourceLocation did = ArtificeRegistry.DATA.getKey(this);
					displayName = Component.literal(did != null ? did.toString() : "Generated Data");
					return displayName.getString();
				}
			}
		}
		return displayName.getString();
	}

	public static PackSource ARTIFICE_RESOURCE_PACK_SOURCE = PackSource.create(
			component -> Component.translatable("pack.source.artifice"),
			false
	);

	@Override
	@Environment(EnvType.CLIENT)
	public ClientOnly<Pack> toClientResourcePackProfile() {
		Pack profile;
		String id = identifier == null ? "null" : identifier.toString();
		if (!this.overwrite) {
			profile = new ArtificeResourcePackContainer(this.optional, this.visible, Objects.requireNonNull(Pack.create(
					id, displayName,
					false, string -> ArtificeResourcePackImpl.this, new Pack.Info(description, 12, FeatureFlagSet.of()),
					PackType.CLIENT_RESOURCES, this.optional ? Pack.Position.TOP : Pack.Position.BOTTOM,
					false, ARTIFICE_RESOURCE_PACK_SOURCE
			)));
		} else {
			profile = new ArtificeResourcePackContainer(false, false, Objects.requireNonNull(Pack.create(
					id, displayName,
					true, string -> ArtificeResourcePackImpl.this, new Pack.Info(description, 12, FeatureFlagSet.of()),
					PackType.CLIENT_RESOURCES, Pack.Position.TOP,
					false, ARTIFICE_RESOURCE_PACK_SOURCE
			)));
		}


		return new ClientOnly<>(profile);
	}

	@Environment(EnvType.CLIENT)
	public ArtificeResourcePackContainer getAssetsContainer() {
		return (ArtificeResourcePackContainer) toClientResourcePackProfile().get();
	}

	@Override
	public Pack toServerResourcePackProfile() {
		String id = identifier == null ? "null" : identifier.toString();
		return Pack.create(
				id, displayName,
				!optional, string -> ArtificeResourcePackImpl.this, new Pack.Info(description, 11, FeatureFlagSet.of()),
				PackType.SERVER_DATA, Pack.Position.BOTTOM,
				false, ARTIFICE_RESOURCE_PACK_SOURCE
		);
	}

	public Pack getDataContainer() {
		return toServerResourcePackProfile();
	}

	private class Memoized<T> implements Supplier<byte[]> {
		private final BiFunction<ArtificeResourcePack, T, byte[]> func;
		private final T path;
		private byte[] data;

		public Memoized(BiFunction<ArtificeResourcePack, T, byte[]> func, T path) {
			this.func = func;
			this.path = path;
		}

		@Override
		public byte[] get() {
			if (this.data == null) {
				this.data = func.apply(ArtificeResourcePackImpl.this, path);
			}
			return this.data;
		}
	}

	@Override
	public Future<?> async(Consumer<ArtificeResourcePack> action) {
		this.lock();
		return EXECUTOR_SERVICE.submit(() -> {
			action.accept(this);
			this.waiting.unlock();
		});
	}

	private void lock() {
		if (!this.waiting.tryLock()) {
			this.waiting.lock();
		}
	}
}
