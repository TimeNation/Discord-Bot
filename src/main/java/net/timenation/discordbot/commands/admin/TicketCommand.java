package net.timenation.discordbot.commands.admin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import net.timenation.discordbot.DiscordBOT;

import java.awt.*;

public class TicketCommand extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();
        TextChannel channel = event.getTextChannel();

        if (event.isFromType(ChannelType.TEXT)) {
            if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                if (message.startsWith("!sendticketembed")) {
                    channel.purgeMessages(DiscordBOT.get(channel, 0));
                    EmbedBuilder embedBuilder = new EmbedBuilder();

                    embedBuilder.setTitle("> Ticket-System :tickets:");
                    embedBuilder.setColor(Color.GREEN);
                    embedBuilder.setDescription("Hier findest du alles rund um das TimeNation Ticket-System! \n \n**Der Ticket-Support ist __auschließlich__ für die unten genannten Dinge** \n> <:timenation_logo:960192143195013141> »» Frage zum TimeNation Netzwerk \n> <:poop_error:960196672921698305> »» Melde einen Bug \n> <:apply:960197085616037959> »» Als Teammitglied bewerben \n> <:apply_vip:960204240771481710> »» Als VIP bewerben \n \n Du bist sicher, das du ein Ticket eröffnen willst? \n Dann klicke auf den 'Öffne ein Ticket' Button!");
                    embedBuilder.setFooter("TimeNation System by TimeNation (ByRaudy)", "https://cdn.discordapp.com/attachments/819892883711983618/868052405298790401/TimeNation-Avatar.png");

                    channel.sendMessageEmbeds(embedBuilder.build()).setActionRow(Button.primary("open_ticket", "Eröffne ein Ticket")).queue();
                }
            }

        }
    }
}
