package net.timenation.discordbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.timenation.discordbot.DiscordBOT;

import java.util.concurrent.TimeUnit;

public class MembersCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if(event.getOption("option") != null && event.getOption("option").getAsString().equalsIgnoreCase("members")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("> Mitglieder <:timenation_logo:960192143195013141>");
            embedBuilder.setDescription("Hey " + event.getMember().getAsMention() + "! \n \n Der TimeNation Discord hat **" + DiscordBOT.getInstance().getJda().getUsers().size() + "** User.");
            embedBuilder.setFooter("TimeNation System by TimeNation (ByRaudy)", "https://cdn.discordapp.com/attachments/819892883711983618/868052405298790401/TimeNation-Avatar.png");

            event.replyEmbeds(embedBuilder.build()).complete().deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
        }
    }
}
