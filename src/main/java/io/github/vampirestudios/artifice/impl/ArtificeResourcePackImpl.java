package io.github.vampirestudios.artifice.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.bridge.game.PackType;
import io.github.vampirestudios.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.artifice.api.builder.JsonObjectBuilder;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.resource.ArtificeResource;
import io.github.vampirestudios.artifice.api.resource.JsonResource;
import io.github.vampirestudios.artifice.api.util.IdUtils;
import io.github.vampirestudios.artifice.api.virtualpack.ArtificeResourcePackContainer;
import io.github.vampirestudios.artifice.common.ArtificeRegistry;
import io.github.vampirestudios.artifice.common.ClientOnly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import org.apache.commons.io.input.NullInputStream;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ArtificeResourcePackImpl implements ArtificeResourcePack {
    private final net.minecraft.server.packs.PackType type;
    @Nullable
    private final ResourceLocation identifier;
    private final Set<String> namespaces = new HashSet<>();
    private final Map<ResourceLocation, ArtificeResource<?>> resources = new HashMap<>();
    private final Set<LanguageInfo> languages = new HashSet<>();
    private final JsonResource<JsonObject> metadata;

    private String description;
    private String displayName;
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
                    .add("pack_format", SharedConstants.getCurrentVersion().getPackVersion(PackType.RESOURCE))
                    .add("description", description != null ? description : "In-memory resource pack created with Artifice")
                    .build();
        } else {
            packMeta = new JsonObjectBuilder()
                    .add("pack_format", SharedConstants.getCurrentVersion().getPackVersion(PackType.DATA))
                    .add("description", description != null ? description : "In-memory data pack created with Artifice")
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

    public void dumpResources(String folderPath) throws IOException, IllegalArgumentException {
        LogManager.getLogger().info("[Artifice] Dumping " + getName() + " " + type.getDirectory() + " to " + folderPath + ", this may take a while.");

        File dir = new File(folderPath);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Can't dump resources to " + folderPath + "; couldn't create parent directories");
        }
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Can't dump resources to " + folderPath + " as it's not a directory");
        }
        if (!dir.canWrite()) {
            throw new IOException("Can't dump resources to " + folderPath + "; permission denied");
        }

        new Thread(() -> {
            writeResourceFile(new File(folderPath + "/pack.mcmeta"), metadata);
            resources.forEach((id, resource) -> {
                String path = String.format("./%s/%s/%s/%s", folderPath, this.type.getDirectory(), id.getNamespace(), id.getPath());
                writeResourceFile(new File(path), resource);
            });
            LogManager.getLogger().info("[Artifice] Finished dumping " + getName() + " " + type.getDirectory() + ".");
        }).start();
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

    @EnvironmentInterface(value = EnvType.CLIENT, itf = ClientResourcePackBuilder.class)
    private final class ArtificeResourcePackBuilder implements ClientResourcePackBuilder, ServerResourcePackBuilder {
        private ArtificeResourcePackBuilder() {
        }

        @Override
        public void setDisplayName(String name) {
            ArtificeResourcePackImpl.this.displayName = name;
        }

        @Override
        public void setDescription(String desc) {
            ArtificeResourcePackImpl.this.description = desc;
        }

        @Override
        public void dumpResources(String filePath, String type) throws IOException {
            LogManager.getLogger().info("[Artifice] Dumping " + getName() + " " + type + " to " + filePath + ", this may take a while.");

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
                        .add("pack_format", SharedConstants.getCurrentVersion().getPackVersion(PackType.RESOURCE))
                        .add("description", description != null ? description : "In-memory resource pack created with Artifice")
                        .build();
            } else {
                packMeta = new JsonObjectBuilder()
                        .add("pack_format", SharedConstants.getCurrentVersion().getPackVersion(PackType.DATA))
                        .add("description", description != null ? description : "In-memory data pack created with Artifice")
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
                resources.forEach((id, resource) -> {
                    String path = String.format("./%s/%s/%s/%s", filePath, type, id.getNamespace(), id.getPath());
                    writeResourceFile(new File(path), resource);
                });
                LogManager.getLogger().info("[Artifice] Finished dumping " + getName() + " " + type + ".");
            }).start();
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
        public void addLanguage(LanguageInfo def) {
            ArtificeResourcePackImpl.this.languages.add(def);
        }

        @Environment(EnvType.CLIENT)
        @Override
        public void addLanguage(String code, String region, String name, boolean rtl) {
            this.addLanguage(new LanguageInfo(code, region, name, rtl));
        }


        @Override
        public <T extends TypedJsonObject> void add(String path, ResourceLocation id, T f) {
            this.add(IdUtils.wrapPath(path, id, ".json"), new JsonResource(f.build()));
        }

        @Override
        public <T extends TypedJsonObject> void add(String path, ResourceLocation id, String ext, T f) {
            this.add(IdUtils.wrapPath(path, id, ext), new JsonResource(f.build()));
        }
    }

    @Override
    public InputStream getRootResource(String fname) {
        if (fname.equals("pack.mcmeta")) return metadata.toInputStream();
        return new NullInputStream(0);
    }

    @Override
    public InputStream getResource(net.minecraft.server.packs.PackType type, ResourceLocation id) throws IOException {
        if (!hasResource(type, id)) throw new FileNotFoundException(id.getPath());
        return this.resources.get(id).toInputStream();
    }

    @Override
    public Collection<ResourceLocation> getResources(net.minecraft.server.packs.PackType type, String namespace, String prefix, int maxDepth, Predicate<String> pathFilter) {
        if (type != this.type) return new HashSet<>();
        Set<ResourceLocation> keys = new HashSet<>(this.resources.keySet());
        keys.removeIf(id -> !id.getPath().startsWith(prefix) || !pathFilter.test(id.getPath()));
        return keys;
    }

    @Override
    public boolean hasResource(net.minecraft.server.packs.PackType type, ResourceLocation id) {
        return type == this.type && this.resources.containsKey(id);
    }

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
    public boolean isShouldOverwrite(){
        return this.overwrite;
    }

    @Override
    public void close() {
    }

    @Override
    public String getName() {
        if (displayName == null) {
            switch (this.type) {
                case CLIENT_RESOURCES -> {
                    ResourceLocation aid = ArtificeRegistry.ASSETS.getKey(this);
                    return displayName = aid != null ? aid.toString() : "Generated Resources";
                }
                case SERVER_DATA -> {
                    ResourceLocation did = ArtificeRegistry.DATA.getKey(this);
                    return displayName = did != null ? did.toString() : "Generated Data";
                }
            }
        }
        return displayName;
    }

    public static PackSource ARTIFICE_RESOURCE_PACK_SOURCE = PackSource.decorating("pack.source.artifice");

    @Override
    @Environment(EnvType.CLIENT)
    public ClientOnly<Pack> toClientResourcePackProfile(Pack.PackConstructor factory) {
        Pack profile;
        String id = identifier == null ? "null" : identifier.toString();
        if (!this.overwrite) {
             profile = new ArtificeResourcePackContainer(this.optional, this.visible, Objects.requireNonNull(Pack.create(
                     id,
                    false, () -> this, factory,
                    this.optional ? Pack.Position.TOP : Pack.Position.BOTTOM,
                    ARTIFICE_RESOURCE_PACK_SOURCE
            )));
        } else {
            profile = new ArtificeResourcePackContainer(false, false, Objects.requireNonNull(Pack.create(
                    id,
                    true, () -> this, factory,
                    Pack.Position.TOP,
                    ARTIFICE_RESOURCE_PACK_SOURCE
            )));
        }


        return new ClientOnly<>(profile);
    }

    @Environment(EnvType.CLIENT)
    public ArtificeResourcePackContainer getAssetsContainer(Pack.PackConstructor factory) {
        return (ArtificeResourcePackContainer) toClientResourcePackProfile(factory).get();
    }

    @Override
    public Pack toServerResourcePackProfile(Pack.PackConstructor factory) {
        return Pack.create(
                identifier == null ? "null" : identifier.toString(),
                !optional, () -> this, factory,
                Pack.Position.BOTTOM,
                ARTIFICE_RESOURCE_PACK_SOURCE
        );
    }

    public Pack getDataContainer(Pack.PackConstructor factory) {
        return toServerResourcePackProfile(factory);
    }
}
