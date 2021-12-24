package me.xelementzx.velocityuuidgetter.commands;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.util.UuidUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import java.util.stream.Collectors;

public class GetUuidCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length != 1) {
            source.sendMessage(Component.text("Invalid Usage!", NamedTextColor.RED));
            source.sendMessage(Component.text("Usage: /getuuid <player>", NamedTextColor.RED));
        }
        if (args.length == 1) {
            String playerName = args[0];
            String playerOfflineUniqueID = UuidUtils.toUndashed(UuidUtils.generateOfflinePlayerUuid(playerName));
            String playerOnlineUniqueID = getOnlineUuid(playerName.toLowerCase()).flatMap(jsonObject -> parseJson(jsonObject.get("id"), String.class)).orElse(null);
            source.sendMessage(Component.text("UUID of: " + playerName, NamedTextColor.GREEN));
            if (playerOnlineUniqueID == null ) {
                source.sendMessage(Component.text("Getting Online UUID Failed!", NamedTextColor.RED));
            } else {
                source.sendMessage(Component.text("Online UUID: " + playerOnlineUniqueID, NamedTextColor.GREEN));
            }
            source.sendMessage(Component.text("Offline UUID: " + playerOfflineUniqueID, NamedTextColor.GREEN));
        }
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        invocation.source().sendMessage(Component.text("You do not have permission to execute this command!", NamedTextColor.RED));
        return invocation.source().hasPermission("velocityuuidgetter.command.getuuid");
    }

    private Optional<JsonObject> getOnlineUuid(String username) {
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

    private <T> Optional<T> parseJson(String json, Class<T> type) {
        try {
            return parseJson(JsonParser.parseString(json), type);
        } catch (RuntimeException ex) {
            return Optional.empty();
        }
    }

    private <T> Optional<T> parseJson(JsonElement jsonElement, Class<T> type) {
        try {
            return Optional.of(new Gson().fromJson(jsonElement, type));
        } catch (RuntimeException ex) {
            return Optional.empty();
        }
    }
}