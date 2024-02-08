package com.arazadaz.dd.config;

import com.arazadaz.dd.Main;
import com.arazadaz.dd.api.origins.Origin;
import com.arazadaz.dd.api.origins.OriginID;
import com.arazadaz.dd.api.origins.OriginManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();


    private static final ModConfigSpec.BooleanValue SPAWN_TOGGLE = BUILDER
            .comment("Whether to enable the spawn point as an origin(Can be over-ridden by other mods")
            .define("useSpawnOrigin", true);

    private static final ModConfigSpec.BooleanValue UDO_TOGGLE = BUILDER
            .comment("Whether to enable user defined point origins(Can be over-ridden by other mods")
            .define("useUserDefinedOrigins", true);


    private static final ModConfigSpec.IntValue GLOBAL_RANGE = BUILDER
            .comment("Global range of origin points. Range being how far out the defined formulas can take into consideration")
            .defineInRange("Global range", 10000, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.ConfigValue<String> GLOBAL_FORMULAS = BUILDER
            .comment("The default formulas used for Origins if the values weren't specified at their creation. Three formulas: circle, square, and custom in that order")
            .define("Global formulas", "distance/range, max(abs(dstPos.x-srcPos.x)/range, abs(dstPos.y-srcPos.y)/range, 50/100");

    // a list of strings that are treated as resource locations for items
    private static final ModConfigSpec.ConfigValue<List<? extends String>> USER_DEFINED_ORIGINS = BUILDER
            .comment("A list of your custom defined origins. The syntax is as follows:(src.x, src.y, src.z, world, bound-by-range(true/false)[optional], defaultTag(1-3)[optional], tags[optional/seperated by '/'), formulas[optional/seperated by '/']")
            .defineListAllowEmpty("origins", List.of(""), Config::validateOriginEntry);

    public static final ModConfigSpec SPEC = BUILDER.build();

    //Whether to use spawn and user defined origins or not. I might add an entry for mod defined origins as well.
    public static boolean useSpawnOrigin;
    public static boolean useUserDefinedOrigins;

    //Global values
    public static int range;
    public static String formulas;

    private static boolean validateOriginEntry(final Object obj)
    {
        return obj instanceof String itemName && !(itemName.split(",").length>5);
    }

    //TODO implement other possible specs(define expanded optional arg cases)
    private static void mapOrigins(String originSpec){

        String[] args = originSpec.split(",");

        switch(args.length){

            case 3 -> {
                Vec3 srcPos = new Vec3(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                OriginID userOrigin = OriginManager.registerOrigin(srcPos, new String[]{"userOrigin"});

                Main.vault.userOrigins.add(userOrigin);
            }

            default -> throw new IllegalStateException("Unexpected value: " + originSpec.length());
        }
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        useSpawnOrigin = SPAWN_TOGGLE.get();
        useUserDefinedOrigins = UDO_TOGGLE.get();
        range = GLOBAL_RANGE.get();
        formulas = GLOBAL_FORMULAS.get();

        // convert the list of strings into a set of items


         USER_DEFINED_ORIGINS.get()
                .forEach(Config::mapOrigins);
    }
}