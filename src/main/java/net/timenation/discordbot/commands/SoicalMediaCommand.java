package net.timenation.discordbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.timenation.discordbot.DiscordBOT;

import java.util.concurrent.TimeUnit;

public class SoicalMediaCommand extends ListenerAdapter {
    
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if(event.getOption("option") != null && event.getOption("option").getAsString().equalsIgnoreCase("socialmedia")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("> Social Media Kanäle von TimeNation :mobile_phone:");
            embedBuilder.setDescription("Hey " + event.getMember().getAsMention() + "! \n \n **Dies ist die Liste, von allen Social Media Kanälen vom TimeNation Netzwerk.** \n " +
                    "»» **Twitter** | https://www.twitter.com/TimeNationNET \n " +
                    "»» **YouTube** | https://www.youtube.com/channel/UC5IC_t6OsrULJfVVxQcV0Tg \n " +
                    "»» **TikTok** | https://www.tiktok.com/@timenationnetzwerk");
            embedBuilder.setFooter("TimeNation System by TimeNation (ByRaudy)", "https://cdn.discordapp.com/attachments/819892883711983618/868052405298790401/TimeNation-Avatar.png");

            event.replyEmbeds(embedBuilder.build()).complete().deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
        }
    }
}
