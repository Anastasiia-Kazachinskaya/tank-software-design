package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Каждый кадр спрашивает у всех источников их команду и выполняет её.
 * Игрок и AI здесь неразличимы — это просто разные CommandSource.
 */
public class GameLoop {
    private final List<CommandSource> commandSources = new ArrayList<>();

    public GameLoop addCommandSource(CommandSource commandSource) {
        commandSources.add(commandSource);
        return this;
    }

    public void executeCommands(float deltaTime) {
        for (CommandSource commandSource : commandSources) {
            Command command = commandSource.nextCommand(deltaTime);
            if (command != null) {
                command.execute();
            }
        }
    }
}