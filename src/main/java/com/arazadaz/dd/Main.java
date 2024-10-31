package com.arazadaz.dd;

import com.arazadaz.dd.api.origins.OriginID;
import com.arazadaz.dd.api.origins.OriginManager;
import com.arazadaz.dd.commands.DDCommands;
import com.arazadaz.dd.commands.OriginTagArgumentType;
import com.arazadaz.dd.config.Config;
import com.arazadaz.dd.core.DDVault;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Main.MODID)
public class Main
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "dd";

    @SuppressWarnings("null")
    public static final DDVault vault = DDVault.createDDVault();
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();



    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Main(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);



        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (Main) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);



        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        //Register our OriginTagArgumentType
        ArgumentTypeInfos.registerByClass(OriginTagArgumentType.class, SingletonArgumentInfo.contextFree(OriginTagArgumentType::tag));
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        //Testing
        vault.userOrigins.forEach((udo) -> LOGGER.info("USER_ORIGINS >> {}", udo.origin.pos.toString()));
    }

    public void clientSetup(FMLClientSetupEvent event)
    {
        // Some client setup code
    }



    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LevelData data = event.getServer().getLevel(Level.OVERWORLD).getLevel().getLevelData();

        BlockPos spawn = data.getSpawnPos();

        Vec3 spawnVec = new Vec3(spawn.getX(), spawn.getY(), spawn.getZ());

        if(Config.useSpawnOrigin) {
            OriginID spawnOrigin = OriginManager.registerOrigin(spawnVec, "spawn");
            vault.userOrigins.add(spawnOrigin);
        }


    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME)
    public static class gameEvents{
        @SubscribeEvent
        public static void onCommandsRegistry(final RegisterCommandsEvent event) {
            //Utility.debugMsg(0,"Dynamic Difficulty: Registering Command Dispatcher");
            DDCommands.registerCommands(event.getDispatcher());
        }
    }
}
