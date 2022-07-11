package net.timenation.discordbot.manager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.timenation.discordbot.DiscordBOT;
import net.timenation.discordbot.manager.tickettype.TicketType;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

public class TicketManager {

    private final ArrayList<Member> memberList;
    DateTimeFormatter date;
    DateTimeFormatter clock;

    public TicketManager() {
        this.memberList = new ArrayList<>();
        this.date = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.clock = DateTimeFormatter.ofPattern("HH:mm");
    }

    public void createTicket(Guild guild, Member member) {
        Category category = guild.getCategoryById("855586604075843605");
        TextChannel textChannel = category.createTextChannel("ticket-" + member.getUser().getName()).addPermissionOverride(member, Collections.singletonList(Permission.VIEW_CHANNEL), Collections.singletonList(Permission.MESSAGE_WRITE)).addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL)).addPermissionOverride(guild.getRoleById("846730196206878740"), EnumSet.of(Permission.VIEW_CHANNEL), null).complete();
        SelectionMenu selectionMenu = SelectionMenu.create("menu:class")
                .setPlaceholder("Wähle einen TicketType")
                .setRequiredRange(1, 1)
                .addOption("❓ Frage zum Netzwerk ❓", "network_question")
                .addOption("\uD83D\uDC1E Reporte einen Bug \uD83D\uDC1E", "bug_report")
                .addOption("\uD83D\uDCDD Als Teammitglied bewerben \uD83D\uDCDD", "team_apply")
                .addOption("\uD83D\uDCEC Als VIP bewerben \uD83D\uDCEC", "vip_apply")
                .addOption("\uD83D\uDEAB Abbruch \uD83D\uDEAB", "cancel")
                .build();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.setTitle("> Ticket von " + member.getUser().getAsTag() + " :tickets:");
        embedBuilder.setDescription("Hey " + member.getAsMention() + "! Herzlich Willkommen im **Ticket Support** vom [TimeNation Netzwerk](https://dc.timenation.net). \n \n**Bitte wähle einen TicketType** \n> Bitte wähle unten über das sogenannte 'Selection Menu' ein TicketType. Damit wissen die Teammitglieder bescheid, und können dir direkt weiter helfen! ^-^");
        embedBuilder.setFooter("TimeBot by TimeNation (ByRaudy)", "https://cdn.discordapp.com/attachments/819892883711983618/868052405298790401/TimeNation-Avatar.png");
        textChannel.sendMessageEmbeds(embedBuilder.build()).setActionRow(selectionMenu).queue();

        sendCreatedTicketLogEmbed(member, textChannel);
        memberList.add(member);
    }

    public void setTicketType(SelectionMenuEvent event, TextChannel channel, TicketType ticketType) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        channel.deleteMessageById(event.getMessageId()).queue();
        channel.putPermissionOverride(event.getMember()).setAllow(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE).queue();

        switch (ticketType) {
            case QUESTION -> embedBuilder.setTitle("> Frage wegen des TimeNation Netzwerkes <:timenation_logo:960192143195013141>");
            case TEAMAPPLY -> embedBuilder.setTitle("> Bewerben als Teammitglied <:apply:960197085616037959>");
            case VIPAPPLY -> embedBuilder.setTitle("> Bewerben als VIP(YouTuber/Streamer) <:apply_vip:960204240771481710>");
            case BUG_REPORT -> embedBuilder.setTitle("> Bug melden <:poop_error:960196672921698305>");
        }
        embedBuilder.setDescription("**Willkommen im Ticket von " + event.getMember().getAsMention() + ".** \n \n **Ticketoptionen:** \n ✅ »» Schließe das Ticket (Nur Teammitglieder) \n ⚠ »» Ticket weiterleiten (Nur Teammitglieder)");
        embedBuilder.setFooter("TimeBot by TimeNation (ByRaudy)", "https://cdn.discordapp.com/attachments/819892883711983618/868052405298790401/TimeNation-Avatar.png");

        channel.sendMessage(event.getGuild().getRoleById("846730196206878740").getAsMention()).complete().delete().queueAfter(1, TimeUnit.SECONDS);;
        event.replyEmbeds(embedBuilder.build()).addActionRow(Button.success("close_ticket", Emoji.fromMarkdown("✅")), Button.danger("forward_ticket", Emoji.fromMarkdown("⚠"))).queue();
    }

    public void setForwartTicket(ButtonClickEvent event, TextChannel channel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("> Das Ticket wurde weiterleitet. ⚠");
        embedBuilder.setDescription("**Das Ticket wurde von " + event.getMember().getAsMention() + " weiterleitet.** \n \n **Letzte Ticketoption:** \n ✅ »» Schließe das Ticket (Nur Owner/Admin)");
        embedBuilder.setFooter("TimeBot by TimeNation (ByRaudy)", "https://cdn.discordapp.com/attachments/819892883711983618/868052405298790401/TimeNation-Avatar.png");

        event.replyEmbeds(embedBuilder.build()).addActionRow(Button.success("close_ticket", Emoji.fromMarkdown("✅"))).queue();
        event.getInteraction().getMessage().delete().queue();
        channel.sendMessage(event.getGuild().getRoleById("846730190846558248").getAsMention() + event.getGuild().getRoleById("846730191539142686").getAsMention()).complete().delete().queueAfter(1, TimeUnit.SECONDS);
    }

    public void deleteTicket(GenericComponentInteractionCreateEvent event, Member member) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.setTitle("> Das Ticket wurde geschlossen. :tickets:");
        embedBuilder.setDescription("**Das Ticket wurde von " + member.getAsMention() + " geschlossen.** \n \n **Das Ticket wird in 5 Sekunden gelöscht.**");
        embedBuilder.setFooter("TimeBot by TimeNation (ByRaudy)", "https://cdn.discordapp.com/attachments/819892883711983618/868052405298790401/TimeNation-Avatar.png");
        event.replyEmbeds(embedBuilder.build()).queue();
        event.getMessage().delete().queue();
        event.getTextChannel().delete().queueAfter(5, TimeUnit.SECONDS);
        sendClosedTicketLogEmbed(member, event.getTextChannel());
        memberList.remove(member);
    }

    public void sendCreatedTicketLogEmbed(Member member, TextChannel textChannel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("> Es wurde ein Ticket eröffnet. :inbox_tray:");
        embedBuilder.setDescription(member.getAsMention() + " hat ein Ticket am " + date.format(LocalDateTime.now()) + " um " + clock.format(LocalDateTime.now()) + " eröffnet.");
        embedBuilder.setFooter("TimeBot by TimeNation (ByRaudy)", "https://cdn.discordapp.com/attachments/819892883711983618/868052405298790401/TimeNation-Avatar.png");
        DiscordBOT.getInstance().getJda().getTextChannelById("854101835174772768").sendMessageEmbeds(embedBuilder.build()).queue();
    }

    public void sendClosedTicketLogEmbed(Member member, TextChannel textChannel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("> Es wurde ein Ticket geschlossen. :outbox_tray:");
        embedBuilder.setDescription(member.getAsMention() + " hat das Ticket '#" + textChannel.getName() + "' am " + date.format(LocalDateTime.now()) + " um " + clock.format(LocalDateTime.now()) + " geschlossen.");
        embedBuilder.setFooter("TimeBot by TimeNation (ByRaudy)", "https://cdn.discordapp.com/attachments/819892883711983618/868052405298790401/TimeNation-Avatar.png");

        DiscordBOT.getInstance().getJda().getTextChannelById("854101835174772768").sendMessageEmbeds(embedBuilder.build()).queue();
    }

    public ArrayList<Member> getMemberList() {
        return memberList;
    }
}
