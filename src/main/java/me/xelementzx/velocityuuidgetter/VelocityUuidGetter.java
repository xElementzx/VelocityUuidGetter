package me.xelementzx.velocityuuidgetter;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import me.xelementzx.velocityuuidgetter.commands.GetUuidCommand;
import org.slf4j.Logger;

@Plugin(id = "velocityuuidgetter", name = "VelocityUuidGetter", version = "1.0",
        description = "Gets a players online and offline Uuid.", authors = "xElementzx")

@SuppressWarnings("unused")
public class VelocityUuidGetter {
    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public VelocityUuidGetter(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        logger.info("VelocityUuidGetter Loaded!");
        server.getCommandManager().register(new GetUuidCommand(), "getuuid");
    }
}