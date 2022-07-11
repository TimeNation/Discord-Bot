package net.timenation.discordbot.commands.fun;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.http.client.fluent.Request;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PatCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if(event.getName().equals("pat")) {
            if(event.getOption("user") != null) {
                EmbedBuilder embedBuilder = new EmbedBuilder();

                embedBuilder.setTitle("> Pat :pleading_face:");
                embedBuilder.setDescription("Hey " + event.getOption("user").getAsUser().getAsMention() + "! " + event.getMember().getAsMention() + " hat dir deinen Kopf getätschelt.");
                embedBuilder.setImage(getLink().get("link").getAsString());
                embedBuilder.setFooter("TimeNation System by TimeNation (ByRaudy)", "https://cdn.discordapp.com/attachments/819892883711983618/868052405298790401/TimeNation-Avatar.png");

                event.replyEmbeds(embedBuilder.build()).queue();
            } else {
                EmbedBuilder embedBuilder = new EmbedBuilder();

                embedBuilder.setTitle("> Fehler <:poop_error:960196672921698305>");
                embedBuilder.setColor(new Color(156, 0, 0));
                embedBuilder.setDescription("Hey " + event.getUser().getAsMention() + "! \n \n Du hast den /pat Command falsch benutzt! Bitte benutze diesen wie folgt: \n »» /pat <user>");
                embedBuilder.setFooter("TimeNation System by TimeNation (ByRaudy)", "https://cdn.discordapp.com/attachments/819892883711983618/868052405298790401/TimeNation-Avatar.png");

                event.replyEmbeds(embedBuilder.build()).complete().deleteOriginal().queueAfter(10, TimeUnit.SECONDS);
            }
        }
    }

    public JsonObject getLink() {
        try {
            JsonParser jsonParser = new JsonParser();
            String string = Request.Get("https://some-random-api.ml/animu/pat").execute().returnContent().asString();
            return jsonParser.parse(string).getAsJsonObject();
        } catch (IOException e) {
        }

        return null;
    }
}
