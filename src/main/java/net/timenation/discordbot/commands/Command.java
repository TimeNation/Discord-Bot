package net.timenation.discordbot.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public abstract class Command extends ListenerAdapter {

    private final String name;
    private final String description;
    protected final CommandData comandData;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;

        this.comandData = new CommandData(name, description);
    }

    @Override
    public final void onSlashCommand(@NotNull SlashCommandEvent event) {
        if(event.getName().equals(getName())) {
            handleCommand(event);
        }
    }

    protected abstract void handleCommand(SlashCommandEvent event);

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public CommandData getCommandData() {
        return comandData;
    }
}
