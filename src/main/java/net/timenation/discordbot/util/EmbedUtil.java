package net.timenation.discordbot.util;

import net.dv8tion.jda.api.EmbedBuilder;

public class EmbedUtil {

    public static EmbedBuilder createEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setFooter("TimeNation System by ByRaudy & Contributors",
                "https://cdn.discordapp.com/attachments/819892883711983618/868052405298790401/TimeNation-Avatar.png");
        return embedBuilder;
    }

}
