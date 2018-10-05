package me.xelementzx.velocityuuidgetter;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.command.CommandManager;
import me.xelementzx.velocityuuidgetter.commands.getUuid;
import org.slf4j.Logger;

@Plugin(id = "velocityuuidgetter", name = "VelocityUuidGetter", version = "1.0-SNAPSHOT",
        description = "Meh, isn't it obvious", authors = {"xElementzx"})

public class VelocityUuidGetter {
    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public VelocityUuidGetter(CommandManager commandManager, ProxyServer server, Logger logger) {
        commandManager.register(new getUuid(), "getUuid");
        this.server = server;
        this.logger = logger;

        logger.info("Loaded!");
    }
}
