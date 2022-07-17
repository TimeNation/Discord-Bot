package net.timenation.discordbot.config;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TimeConfig {

    private String botToken;

    public static TimeConfig loadConfig(File file) {
        if(!file.exists()) {
            TimeConfig timeConfig = new TimeConfig();
            timeConfig.botToken = "change pls";

            try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                fileOutputStream.write(new Gson().toJson(timeConfig).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return timeConfig;
        }

        try(FileInputStream fileInputStream = new FileInputStream(file)) {
            return new Gson().fromJson(new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8), TimeConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getBotToken() {
        return botToken;
    }
}
