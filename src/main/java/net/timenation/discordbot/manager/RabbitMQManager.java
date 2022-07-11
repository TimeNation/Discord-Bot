package net.timenation.discordbot.manager;

import com.rabbitmq.client.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.timenation.discordbot.DiscordBOT;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RabbitMQManager {

    private final ConnectionFactory connectionFactory;
    private final Connection connection;
    private final Channel channel;

    public RabbitMQManager() throws IOException, TimeoutException {
        this.connectionFactory = new ConnectionFactory();
        this.connectionFactory.setHost("localhost");
        this.connectionFactory.setUsername("admin");
        this.connectionFactory.setPassword("9cL9RttK6Sn5t9Wb4iqgM68jN3Q3KK");
        this.connection = this.connectionFactory.newConnection();
        this.channel = this.connection.createChannel();
        this.channel.exchangeDeclare("discord-status", "fanout", false, true, null);
        this.channel.queueDeclare("discord-bot", false, false, true, null);
        this.channel.queueBind("discord-bot", "discord-status", "");

        this.channel.exchangeDeclare("minecraft-network", "fanout", false, true, null);
        this.channel.queueDeclare("minecraft-reports", false, false, true, null);
        this.channel.queueBind("minecraft-reports", "minecraft-network", "");

        startListeningToRabbitMQ();
    }

    public void sendMessageToRabbitMQ(String exchange, String message) throws IOException {
        this.channel.basicPublish(exchange, "", false, null, message.getBytes());
    }

    public void startListeningToRabbitMQ() throws IOException {
        Logger.getAnonymousLogger().log(Level.INFO, "Start listening to RabbitMQ queue 'discord-bot'");

        this.channel.basicConsume("discord-bot", true, (consumerTag, message) -> {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("> Aktueller Status <:timenation_logo:960192143195013141>");
            embedBuilder.addField("States", "\uD83D\uDD34 »» Offline \n \uD83D\uDFE2 »» Online \n ⚠️ »» Wartungsarbeiten", false);

            switch (new String(message.getBody())) {
                case "minecraft_off" -> {
                    embedBuilder.setDescription("\uD83D\uDD34 **»» Minecraft Netzwerk** \n \n  Letzte Änderung: **" + new SimpleDateFormat("HH:mm:ss | dd.MM.yyyy").format(new Date().getTime()) + "**");
                    DiscordBOT.getInstance().getJda().getTextChannelById("960505125489762327").editMessageEmbedsById("960505181668266044", embedBuilder.build()).queue();
                }
                case "minecraft_on" -> {
                    embedBuilder.setDescription("\uD83D\uDFE2 **»» Minecraft Netzwerk** \n \n Letzte Änderung: **" + new SimpleDateFormat("HH:mm:ss | dd.MM.yyyy").format(new Date().getTime()) + "**");
                    DiscordBOT.getInstance().getJda().getTextChannelById("960505125489762327").editMessageEmbedsById("960505181668266044", embedBuilder.build()).queue();
                }
                case "minecraft_maintenance" -> {
                    embedBuilder.setDescription("⚠️ **»» Minecraft Netzwerk** \n \n Letzte Änderung: **" + new SimpleDateFormat("HH:mm:ss | dd.MM.yyyy").format(new Date().getTime()) + "**");
                    DiscordBOT.getInstance().getJda().getTextChannelById("960505125489762327").editMessageEmbedsById("960505181668266044", embedBuilder.build()).queue();
                }
            }
        }, consumerTag -> {});

        this.channel.basicConsume("minecraft-reports", true, (consumerTag, message) -> {
            String[] information = new String(message.getBody()).split(", ");
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("> Es wurde ein Spieler gemeldet. \uD83D\uDCEF");
            embedBuilder.setDescription("\n \n > **Informationen zum Report** \n Gemeldet: **" + information[0] + "** \n Von: **" + information[1] + "** \n Grund: **" + information[2] + "** \n ID: **" + information[3] + "** \n \n > **Datum & Uhrzeit** \n Datum: **" + new SimpleDateFormat("dd.MM.yyyy").format(new Date().getTime()) + "** \n Uhrzeit: **" + new SimpleDateFormat("HH:mm:ss").format(new Date().getTime()) + "**");

            DiscordBOT.getInstance().getJda().getGuildById("971563406635171910").getTextChannelById("993147386601938954").sendMessageEmbeds(embedBuilder.build()).queue();
        }, consumerTag -> {});
    }

    public Connection getConnection() {
        return connection;
    }

    public Channel getChannel() {
        return channel;
    }
}
