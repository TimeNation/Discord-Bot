package net.timenation.discordbot.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

public class RandomAPIHelper {

    public static String getAnimeLink(String type) {
        try {
            String string = Request.Get("https://some-random-api.ml/animu/" + type).execute().returnContent().asString();
            JsonObject asJsonObject = JsonParser.parseString(string).getAsJsonObject();
            if(!asJsonObject.has("link"))
                return null;
            return asJsonObject.get("link").getAsString();
        } catch (IOException ignored) {}
        return null;
    }


}
