package net.timenation.discordbot.commands.fun;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
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

public class PatCommand extends Command {

    public PatCommand() {
        super("pat", "Streichel einen User! <3");

        comandData.addOption(OptionType.USER, "user", "Der User der gestreichelt werden soll.", true);
    }

    @Override
    protected void handleCommand(SlashCommandEvent event) {
        OptionMapping userOption = event.getOption("user");
        if(userOption == null)
            return;
        if(event.getMember() == null)
            return;
        String link = RandomAPIHelper.getAnimeLink("pat");
        if(link == null)
            return;
        User asUser = userOption.getAsUser();
        EmbedBuilder embedBuilder = EmbedUtil.createEmbed();

        embedBuilder.setTitle("> Pat :pleading_face:");
        embedBuilder.setDescription("Hey " + asUser.getAsMention() + "! " + event.getMember().getAsMention() + " hat dir deinen Kopf getätschelt.");
        embedBuilder.setImage(link);

        event.replyEmbeds(embedBuilder.build()).queue();
    }

}
