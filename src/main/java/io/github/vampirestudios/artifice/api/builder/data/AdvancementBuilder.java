package io.github.vampirestudios.artifice.api.builder.data;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.JsonObjectBuilder;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Builder for advancement files ({@code namespace:advancements/advid.json}).
 * @see <a href="https://minecraft.gamepedia.com/Advancements#JSON_Format" target="_blank">Minecraft Wiki</a>
 */
public final class AdvancementBuilder extends TypedJsonObject {
    public AdvancementBuilder() { super(new JsonObject()); }

    /**
     * Set the display options for this advancement.
     * @param settings A callback which will be passed a {@link Display}.
     * @return this
     */
    public AdvancementBuilder display(Display settings) {
        join("display", settings.build());
        return this;
    }

    /**
     * Set the parent advancement for this to inherit from.
     * @param id The parent advancement ID ({@code namespace:advid}).
     * @return this
     */
    public AdvancementBuilder parent(ResourceLocation id) {
        root.addProperty("parent", id.toString());
        return this;
    }

    /**
     * Add a critera for this advancement to be received.
     * @param name The name of this criteria.
     * @param settings A {@link Criteria}.
     * @return this
     */
    public AdvancementBuilder criteria(String name, Criteria settings) {
        join("criteria", new TypedJsonObject().add(name, settings).build());
        return this;
    }

    /**
     * Set which criteria are required to receive this advancement.
     * Passing multiple critera names will allow the advancement to be received if any of the given critera are completed.
     * Calling this multiple times will add a new set of requirements. Each set must have at least one contained criteria completed
     *  to receive the advancement.
     * If this is not called, all criteria will be required by default.
     *
     * @param anyOf A list of criteria names, any of which can be completed to fulfill this requirement.
     * @return this
     */
    public AdvancementBuilder requirement(String... anyOf) {
        join("requirements", arrayOf(anyOf));
        return this;
    }

    /**
     * Builder for advancement display properties.
     * @see AdvancementBuilder
     */
    public static final class Display extends TypedJsonObject {
        private Display() { super(); }

        /**
         * Set the icon item of this advancement.
         * @param item The item ID.
         * @return this
         */
        public Display icon(ResourceLocation item) { return icon(item, null); }

        /**
         * Set the icon item of this advancement.
         * @param item The item ID.
         * @param nbt A string containing the JSON-serialized NBT of the item.
         * @return this
         */
        public Display icon(ResourceLocation item, String nbt) {
            TypedJsonObject icon = new TypedJsonObject();
            icon.add("item", item.toString());
            if(nbt != null) icon.add("nbt", nbt);
            join("icon", icon.build());
            return this;
        }

        /**
         * Set the title of this advancement.
         * @param title The title.
         * @return this
         */
        public Display title(String title) {
            root.addProperty("title", title);
            return this;
        }

        /**
         * Set the title of this advancement.
         * @param title The title.
         * @return this
         */
        public Display title(Component title) {
            root.add("title", Component.Serializer.toJsonTree(title));
            return this;
        }

        /**
         * Set the frame type of this advancement.
         * @param frame The frame type.
         * @return this
         */
        public Display frame(Frame frame) {
            root.addProperty("frame", frame.name);
            return this;
        }

        /**
         * Set the background texture of this advancement. Only applicable for root advancements.
         * @param id The texture path ({@code namespace:textures/gui/advancements/backgrounds/bgname.png}).
         * @return this
         */
        public Display background(ResourceLocation id) {
            root.addProperty("background", id.toString());
            return this;
        }

        /**
         * Set the description of this advancement.
         * @param desc The description.
         * @return this
         */
        public Display description(String desc) {
            root.addProperty("description", desc);
            return this;
        }

        /**
         * Set the description of this advancement.
         * @param desc The description.
         * @return this
         */
        public Display description(Component desc) {
            root.add("description", Component.Serializer.toJsonTree(desc));
            return this;
        }

        /**
         * Set whether this advancement should show a popup onscreen when received.
         * @param show Whether to show the toast.
         * @return this
         */
        public Display showToast(boolean show) {
            root.addProperty("show_toast", show);
            return this;
        }

        /**
         * Set whether achieving this advancement should post a message in chat.
         * @param announce Whether to announce.
         * @return this
         */
        public Display announceToChat(boolean announce) {
            root.addProperty("announce_to_chat", announce);
            return this;
        }

        /**
         * Set whether this advancement should be hidden from the advancement menu until received.
         * @param hidden Whether to hide.
         * @return this
         */
        public Display hidden(boolean hidden) {
            root.addProperty("hidden", hidden);
            return this;
        }

        /**
         * Options for {@link Display#frame}.
         */
        public enum Frame {
            CHALLENGE("challenge"),
            GOAL("goal"),
            TASK("task");

            /** The name of this frame when outputted to JSON. */
            public final String name;
            Frame(String name) { this.name = name; }
        }
    }

    /**
     * Builder for advancement criteria.
     * @see AdvancementBuilder
     * @see <a href="https://minecraft.gamepedia.com/Advancements#List_of_triggers" target="_blank">Minecraft Wiki</a>
     */
    public static final class Criteria extends TypedJsonObject {
        private Criteria() { super(); }

        /**
         * Set the trigger condition of this criteria.
         * @param id The trigger ID ({@code namespace:triggerid}).
         * @return this
         * @see <a href="https://minecraft.fandom.com/wiki/Advancement/JSON_format#List_of_triggers" target="_blank">Minecraft Wiki</a>
         */
        public Criteria trigger(ResourceLocation id) {
            root.addProperty("trigger", id.toString());
            return this;
        }

        /**
         * Set the condition values for the given trigger.
         * These vary from trigger to trigger, so this falls through to direct JSON building.
         *
         * @param settings A callback which will be passed a {@link JsonObjectBuilder}.
         * @return this
         * @see <a href="https://minecraft.gamepedia.com/Advancements#List_of_triggers" target="_blank">Minecraft Wiki</a>
         */
        public Criteria conditions(JsonObjectBuilder settings) {
            root.add("conditions", settings.build());
            return this;
        }
    }
}
