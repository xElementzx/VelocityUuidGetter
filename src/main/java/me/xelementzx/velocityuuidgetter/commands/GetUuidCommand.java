package me.xelementzx.velocityuuidgetter.commands;

import com.google.gson.*;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.util.UuidUtils;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import java.util.stream.Collectors;

public class GetUuidCommand implements Command {

    @Override
    public void execute(@NonNull CommandSource source, String[] args) {
        if (source.hasPermission("velocityuuidgetter.command.getuuid")) {
            if (args.length != 1) {
                source.sendMessage(TextComponent.of("Invalid Usage!").color(TextColor.RED));
                source.sendMessage(TextComponent.of("Usage: /getuuid <player>").color(TextColor.RED));
            }
            if (args.length == 1) {
                String playerName = args[0];
                String playerOfflineUniqueID = UuidUtils.toUndashed(UuidUtils.generateOfflinePlayerUuid(playerName));
                String playerOnlineUniqueID = getOnlineUuid(playerName.toLowerCase()).flatMap(jsonObject -> parseJson(jsonObject.get("id"), String.class)).orElse(null);
                source.sendMessage(TextComponent.of("UUID of: " + playerName).color(TextColor.GREEN));
                if (playerOnlineUniqueID == null ) {
                    source.sendMessage(TextComponent.of("Getting Online UUID Failed!").color(TextColor.RED));
                } else {
                    source.sendMessage(TextComponent.of("Online UUID: " + playerOnlineUniqueID).color(TextColor.GREEN));
                }
                source.sendMessage(TextComponent.of("Offline UUID: " + playerOfflineUniqueID).color(TextColor.GREEN));
            }
        } else {
            source.sendMessage(TextComponent.of("You do not have permission to execute this command!").color(TextColor.RED));
        }
    }

    private static Optional<JsonObject> getOnlineUuid(String username) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            URLConnection urlConnection = url.openConnection();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                return parseJson(bufferedReader.lines().collect(Collectors.joining()), JsonObject.class);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }
    private static <T> Optional<T> parseJson(String json, Class<T> type) {
        try {
            return parseJson(new JsonParser().parse(json), type);
        } catch (RuntimeException ex) {
            return Optional.empty();
        }
    }

    private static <T> Optional<T> parseJson(JsonElement jsonElement, Class<T> type) {
        try {
            return Optional.of(new Gson().fromJson(jsonElement, type));
        } catch (RuntimeException ex) {
            return Optional.empty();
        }
    }
}