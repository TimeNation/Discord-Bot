package net.timenation.discordbot.commands.fun;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.timenation.discordbot.commands.Command;
import net.timenation.discordbot.util.EmbedUtil;
import net.timenation.discordbot.util.RandomAPIHelper;
import org.apache.http.client.fluent.Request;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HugCommand extends Command {

    public HugCommand() {
        super("hug", "Sende einem User eine Umarmung! <3");

        comandData.addOption(OptionType.USER, "user", "Der User der eine Umarmung erhalten soll.", true);
    }

    @Override
    protected void handleCommand(SlashCommandEvent event) {
        OptionMapping userOption = event.getOption("user");
        if(userOption == null)
            return;
        if(event.getMember() == null)
            return;
        String link = RandomAPIHelper.getAnimeLink("hug");
        if(link == null)
            return;
        User asUser = userOption.getAsUser();
        EmbedBuilder embedBuilder = EmbedUtil.createEmbed();

        embedBuilder.setTitle("> Hug :smiling_face_with_3_hearts:");
        embedBuilder.setDescription("Hey " + asUser.getAsMention() + "! " + event.getMember().getAsMention() + " hat dir eine Umarmung geschickt! :D");
        embedBuilder.setImage(link);

        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
