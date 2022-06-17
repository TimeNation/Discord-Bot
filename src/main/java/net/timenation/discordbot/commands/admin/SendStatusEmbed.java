package net.timenation.discordbot.commands.admin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SendStatusEmbed extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getMessage().getContentDisplay().equals("!sendStatusEmbed") && event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("<:timenation_logo:960192143195013141> | Status");
            embedBuilder.setDescription("\uD83D\uDFE2 »» Minecraft Netzwerk");

            event.getTextChannel().sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }
}
