package net.timenation.discordbot.listener;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.timenation.discordbot.DiscordBot;
import net.timenation.discordbot.manager.tickettype.TicketType;

import java.util.concurrent.TimeUnit;

public class TicketReactionManager extends ListenerAdapter {

    @Override
    public void onSelectionMenu(SelectionMenuEvent event) {
        switch (event.getSelectedOptions().get(0).getValue()) {
            case "network_question" -> DiscordBot.getInstance().getTicketManager().setTicketType(event, event.getTextChannel(), TicketType.QUESTION);
            case "bug_report" -> DiscordBot.getInstance().getTicketManager().setTicketType(event, event.getTextChannel(), TicketType.BUG_REPORT);
            case "team_apply" -> DiscordBot.getInstance().getTicketManager().setTicketType(event, event.getTextChannel(), TicketType.TEAMAPPLY);
            case "vip_apply" -> DiscordBot.getInstance().getTicketManager().setTicketType(event, event.getTextChannel(), TicketType.VIPAPPLY);
            case "cancel" -> DiscordBot.getInstance().getTicketManager().deleteTicket(event, event.getMember());
        }
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        switch (event.getButton().getId()) {
            case "open_ticket" -> {
                if (!DiscordBot.getInstance().getTicketManager().getMemberList().contains(event.getMember())) DiscordBot.getInstance().getTicketManager().createTicket(event.getGuild(), event.getMember());
                else event.getInteraction().deferReply().complete().editOriginal("Du hast bereits ein Ticket eröffnet!").complete().delete().queueAfter(5, TimeUnit.SECONDS);
            }
            case "close_ticket" -> DiscordBot.getInstance().getTicketManager().deleteTicket(event, event.getMember());
            case "forward_ticket" -> DiscordBot.getInstance().getTicketManager().setForwartTicket(event, event.getTextChannel());
        }
    }
}
