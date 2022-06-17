package net.timenation.discordbot.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MemberJoinEvent extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        TextChannel channel = guild.getTextChannelById("846730208554778664");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("> Willkommen " + event.getMember().getUser().getAsTag() + " :wave:");
        embedBuilder.setThumbnail(event.getMember().getEffectiveAvatarUrl());
        embedBuilder.setDescription("Herzlich Willkommen " + event.getMember().getAsMention() + " auf dem offiziellen [TimeNation Discord](https://dc.timenation.net). \n \n **Das könnte dich interessieren:** \n> Ticket erstellen: " + guild.getTextChannelById("846730217304883230").getAsMention() + "\n> Neuigkeiten: " + guild.getTextChannelById("848906423543136266").getAsMention() + "\n> Aktueller Netzwerkstatus: " + guild.getTextChannelById("960505125489762327").getAsMention());
        embedBuilder.setFooter("TimeBot by TimeNation (ByRaudy)", "https://cdn.discordapp.com/attachments/819892883711983618/868052405298790401/TimeNation-Avatar.png");

        event.getMember().getGuild().addRoleToMember(event.getMember(), guild.getRoleById("846730202276167690")).queue();
        channel.sendMessageEmbeds(embedBuilder.build()).queue();
    }
}