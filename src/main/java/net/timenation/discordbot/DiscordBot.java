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
import net.timenation.discordbot.config.TimeConfig;
import net.timenation.discordbot.listener.MemberJoinEvent;
import net.timenation.discordbot.listener.TicketReactionManager;
import net.timenation.discordbot.manager.RabbitMQManager;
import net.timenation.discordbot.manager.TicketManager;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class DiscordBot {

    private final TimeConfig timeConfig = TimeConfig.loadConfig(new File("config.json"));

    public static DiscordBot instance;
    private final JDA jda;
    private final TicketManager ticketManager;
    private RabbitMQManager rabbitMQManager;

    public static void main(String[] args) {
        try {
            new DiscordBot();
        } catch (LoginException | IllegalArgumentException ignored) {
        }
    }

    public DiscordBot() throws LoginException, IllegalArgumentException {
        instance = this;
        ticketManager = new TicketManager();
//        try {
//            rabbitMQManager = new RabbitMQManager();
//        } catch (IOException | TimeoutException exception) {
//            exception.printStackTrace();
//        }
        JDABuilder builder = JDABuilder.createDefault(timeConfig.getBotToken(),
                GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES);

        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("auf TimeNation.net"));

        List<Command> commands = List.of(new TimeNationCommand(), new HugCommand(), new PatCommand());

        for (Command command : commands) {
            builder.addEventListeners(command);
        }


        jda = builder.build();
        for (Command command : commands) {
            jda.upsertCommand(command.getCommandData()).queue();
        }

        System.out.println("The TimeBOT is now online. (TimeBot by TimeNation (ByRaudy))");
//        shutdown();
    }

    public TimeConfig getTimeConfig() {
        return timeConfig;
    }

    public static DiscordBot getInstance() {
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
