package net.timenation.discordbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.timenation.discordbot.DiscordBot;
import net.timenation.discordbot.util.EmbedUtil;

import java.util.concurrent.TimeUnit;

public class TimeNationCommand extends Command {

    public TimeNationCommand() {
        super("timenation", "Informationen über TimeNation");

        comandData.addSubcommands(new SubcommandData("partner", "Liste von allen Partnern."),
                new SubcommandData("socialmedia", "Liste aller Social Media Kanäle von TimeNation."),
                new SubcommandData("members", "Zeige, wie viele User auf dem TimeNation Discord sind."));
    }

    @Override
    public void handleCommand(SlashCommandEvent event) {
        if(event.getSubcommandName() == null)
            return;
        EmbedBuilder embedBuilder = EmbedUtil.createEmbed();

        if(event.getSubcommandName().equalsIgnoreCase("members")) {

            embedBuilder.setTitle("> Mitglieder <:timenation_logo:960192143195013141>");
            embedBuilder.setDescription("Hey " + event.getMember().getAsMention() + "! \n \n Der TimeNation Discord hat **" + DiscordBot.getInstance().getJda().getUsers().size() + "** User.");

        } else if(event.getSubcommandName().equalsIgnoreCase("socialmedia")) {
            embedBuilder.setTitle("> Social Media Kanäle von TimeNation :mobile_phone:");

            embedBuilder.setDescription("Hey " + event.getMember().getAsMention() + "! \n \n **Dies ist die Liste, von allen Social Media Kanälen vom TimeNation Netzwerk.** \n " +
                                        "»» **Twitter** | https://www.twitter.com/TimeNationNET \n " +
                                        "»» **YouTube** | https://www.youtube.com/channel/UC5IC_t6OsrULJfVVxQcV0Tg \n " +
                                        "»» **TikTok** | https://www.tiktok.com/@timenationnetzwerk");

        } else if(event.getSubcommandName().equalsIgnoreCase("partner")) {

            embedBuilder.setTitle("> Unsere Partner :handshake:");
            embedBuilder.setDescription("Hey " + event.getMember().getAsMention() + "! \n \n **Hier sind alle Partner von dem TimeNation Netzwerk gelistet.** \n " +
                                        "<:virtualclient:960543029641510942> »» VirtualClient | Minecraft Client");

        }
        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }
}
