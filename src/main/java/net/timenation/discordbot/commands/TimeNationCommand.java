package net.timenation.discordbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class TimeNationCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if(event.getName().equals("timenation") && event.getOption("option") == null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("> Hilfe :books:");
            embedBuilder.setDescription("\n Hey " + event.getMember().getAsMention() + "! \n Du brauchst Hilfe? Gar kein Problem! \n \n " +
                    "**Nützliche Commands:** \n »» **/timenation help** | Liste von allen Commands. \n »» **/timenation partner** | Liste von allen Partnern." +
                    "\n »» **/timenation socialmedia** | Liste aller Social Media Kanäle von TimeNation. \n »» **/timenation members** | Zeige, wie viele User auf dem TimeNation Discord sind." +
                    " \n \n **Spaß Commands:** \n »» **/hug @user** | Umarme einen User. \n »» **/pat @user** | Streichel den Kopf eines Users.");

            embedBuilder.setFooter("TimeNation System by TimeNation (ByRaudy)", "https://cdn.discordapp.com/attachments/819892883711983618/868052405298790401/TimeNation-Avatar.png");

            event.replyEmbeds(embedBuilder.build()).complete().deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
        }
    }
}
