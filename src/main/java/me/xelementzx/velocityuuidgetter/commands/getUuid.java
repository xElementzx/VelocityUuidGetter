package me.xelementzx.velocityuuidgetter.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.util.UuidUtils;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

public class getUuid implements Command {

    @Override
    public void execute(@NonNull CommandSource source, String[] args) {
        if (args.length != 1) {
            source.sendMessage(TextComponent.of("Invalid Usage!").color(TextColor.RED));
            source.sendMessage(TextComponent.of("Usage: /getuuid <player>").color(TextColor.RED));
        }

        if (args.length == 1) {
            String playerName = args[0];
            String playerOfflineUniqueID = UuidUtils.toUndashed(UuidUtils.generateOfflinePlayerUuid(playerName));
            source.sendMessage(TextComponent.of("UUID of: " + playerName).color(TextColor.GREEN));
            /*source.sendMessage(TextComponent.of("Online UUID: " + playerOnlineUniqueID).color(TextColor.GREEN));*/
            source.sendMessage(TextComponent.of("Offline UUID: " + playerOfflineUniqueID).color(TextColor.RED));
        }
    }
}