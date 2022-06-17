package net.timenation.discordbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.timenation.discordbot.commands.*;
import net.timenation.discordbot.commands.admin.SendStatusEmbed;
import net.timenation.discordbot.commands.fun.HugCommand;
import net.timenation.discordbot.commands.fun.PatCommand;
import net.timenation.discordbot.commands.admin.TicketCommand;
import net.timenation.discordbot.listener.MemberJoinEvent;
import net.timenation.discordbot.listener.TicketReactionManager;
import net.timenation.discordbot.manager.RabbitMQManager;
import net.timenation.discordbot.manager.TicketManager;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class DiscordBOT {

    public static DiscordBOT instance;
    private final JDA jda;
    private final TicketManager ticketManager;
    private RabbitMQManager rabbitMQManager;
    private static boolean shutdown = false;

    public static void main(String[] args) {
        try {
            new DiscordBOT();
        } catch (LoginException | IllegalArgumentException ignored) {
        }
    }

    public DiscordBOT() throws LoginException, IllegalArgumentException {
        instance = this;
        ticketManager = new TicketManager();
        try {
            rabbitMQManager = new RabbitMQManager();
        } catch (IOException | TimeoutException exception) {
            exception.printStackTrace();
        }
        JDABuilder builder = JDABuilder.create("ODUzMDY0ODc5MDQ3OTY2NzIw.YMP8lA.P8NOHy0eGCRAIy15LuzIWNGXKWU", GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.listening("@TimeNationNET"));

        builder.addEventListeners(new TimeNationCommand());
        builder.addEventListeners(new HelpCommand());
        builder.addEventListeners(new PartnerCommand());
        builder.addEventListeners(new SoicalMediaCommand());
        builder.addEventListeners(new MembersCommand());
        builder.addEventListeners(new SendStatusEmbed());
        builder.addEventListeners(new HugCommand());
        builder.addEventListeners(new PatCommand());
        builder.addEventListeners(new MemberJoinEvent());
        builder.addEventListeners(new TicketCommand());
        builder.addEventListeners(new TicketReactionManager());

        jda = builder.build();
        jda.upsertCommand("hug", "Sende einem User eine Umarmung! <3").addOption(OptionType.USER, "user", "Wähle einen User").queue();
        jda.upsertCommand("pat", "Streichel einen User! <3").addOption(OptionType.USER, "user", "Wähle einen User").queue();
        jda.upsertCommand("timenation", "Rund um das TimeNation Netzwerk").addOption(OptionType.STRING, "option", "Wähle eine Option: help, socialmedia, members, partner").queue();
        System.out.println("The TimeBOT is now online. (TimeBot by TimeNation (ByRaudy))");
        shutdown();
    }

    public static DiscordBOT getInstance() {
        return instance;
    }

    public JDA getJda() {
        return jda;
    }

    public TicketManager getTicketManager() {
        return ticketManager;
    }

    public RabbitMQManager getRabbitMQManager() {
        return rabbitMQManager;
    }

    private void shutdown() {
        new Thread(() -> {
            String line = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                while ((line = reader.readLine()) != null) {
                    if (line.equalsIgnoreCase("shutdown") || line.equalsIgnoreCase("stop")) {
                        shutdown = true;
                        if (jda != null) {
                            sendShutdownEmbed(jda);
                            jda.getPresence().setStatus(OnlineStatus.OFFLINE);
                            jda.shutdown();
                            System.exit(0);
                        }
                        reader.close();
                    } else {
                        System.out.println("Please use 'shutdown' or 'stop' to shutdown the bot.");
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }).start();
    }

    public void instantShutdown(JDA jda) throws IOException, InterruptedException {
        rabbitMQManager.sendMessageToRabbitMQ("ticketsystem_offline");
        Thread.sleep(2000);
        jda.getPresence().setStatus(OnlineStatus.OFFLINE);
        jda.shutdown();
        System.exit(0);
    }

    public void sendShutdownEmbed(JDA jda) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(":hotsprings: | Shutdown");
        embedBuilder.setDescription("The TimeBot was shutdowned by **Console**.");

        embedBuilder.setFooter("TimeBot by TimeNation (ByRaudy)", "https://cdn.discordapp.com/attachments/819892883711983618/868052405298790401/TimeNation-Avatar.png");

        jda.getTextChannelById("854101835174772768").sendMessageEmbeds(embedBuilder.build()).queue();
    }

    public static List<Message> get(MessageChannel channel, int amount) {
        List<Message> messages = new ArrayList<>();
        int i = amount;

        for (Message message : channel.getIterableHistory().cache(false)) {
            if (!message.isPinned()) {
                messages.add(message);
            }
            if (--i < -0) break;
        }

        return messages;
    }
}
