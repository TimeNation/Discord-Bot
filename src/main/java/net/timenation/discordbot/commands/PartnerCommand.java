package net.timenation.discordbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.timenation.discordbot.DiscordBOT;
import okhttp3.internal.annotations.EverythingIsNonNull;

import java.util.concurrent.TimeUnit;

public class PartnerCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if(event.getOption("option") != null && event.getOption("option").getAsString().equalsIgnoreCase("partner")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle(":handshake: | Partner");
            embedBuilder.setDescription("Hey " + event.getMember().getAsMention() + "! \n \n **Hier sind alle Partner von dem TimeNation Netzwerk gelistet.** \n " +
                    "<:virtualclient:960543029641510942> »» VirtualClient | Minecraft Client \n <:venocix:853385126489882644> »» Venocix | Server Hosting");
            embedBuilder.setFooter("TimeNation System by TimeNation (ByRaudy)", "https://cdn.discordapp.com/attachments/819892883711983618/868052405298790401/TimeNation-Avatar.png");

            event.replyEmbeds(embedBuilder.build()).complete().deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
        }
    }
}
