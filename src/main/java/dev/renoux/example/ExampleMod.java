package dev.renoux.example;

import net.minecraft.server.MinecraftServer;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.ModMetadata;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
    public static ModMetadata metadata;
    public static Logger LOGGER;

    @Override
    public void onInitialize(ModContainer mod) {
        metadata = mod.metadata();
        LOGGER = LoggerFactory.getLogger(metadata.id());

        LOGGER.info("{} : LOADING", metadata.name());

        LOGGER.info("{} : LOADED", metadata.name());
    }
}
