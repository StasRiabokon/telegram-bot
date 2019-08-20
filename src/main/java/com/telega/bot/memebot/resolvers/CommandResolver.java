package com.telega.bot.memebot.resolvers;

import com.telega.bot.memebot.commands.abstracts.MultistageAbstractCommand;
import com.telega.bot.memebot.commands.impl.sipmle.DefaultCommand;
import com.telega.bot.memebot.commands.interfaces.Command;
import com.telega.bot.memebot.commands.interfaces.MultistageCommand;
import com.telega.bot.memebot.invokers.CommandInvoker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public class CommandResolver {

    private final CommandInvoker commandInvoker;
    private final DefaultCommand defaultCommand;

    private List<MultistageAbstractCommand> activeCommands = new ArrayList<>();

    private Map<String, Command> commands;

    public void resolve(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            resolveCommandByName(message.getText(), update);
        }
    }

    private void resolveCommandByName(String name, Update update) {
        if (isNotMultistageCommand(name)) {
            commandInvoker.invokeSimpleCommand(getSimpleCommand(name), update);
        } else {
            Optional<MultistageAbstractCommand> activeCommand = getActiveCommand();
            if (activeCommand.isPresent()) {
                commandInvoker.initMultistageCommand(activeCommand.get(), update);
                Optional<MultistageAbstractCommand> readyCommand = getReadyCommand();
                readyCommand.ifPresent(command -> {
                    commandInvoker.executeMultistageCommand(command, update);
                    activeCommands.remove(command);
                });
            } else {
                MultistageAbstractCommand multistageCommand = getMultistageCommand(name);
                addMultistageCommand(multistageCommand);
                commandInvoker.initMultistageCommand(multistageCommand, update);
            }
        }
    }

    private void addMultistageCommand(MultistageAbstractCommand multistageCommand) {
        multistageCommand.initSteps();
        multistageCommand.initAnswers();
        activeCommands.add(multistageCommand);
    }

    private boolean isNotMultistageCommand(String name) {
        return commands.values()
                .stream()
                .anyMatch(c -> !(c instanceof MultistageCommand) && c.getName().equals(name));

    }

    private MultistageAbstractCommand getMultistageCommand(String s) {
        return (MultistageAbstractCommand) commands.getOrDefault(s, defaultCommand);
    }

    private Optional<MultistageAbstractCommand> getActiveCommand() {
        return activeCommands
                .stream()
                .filter(MultistageAbstractCommand::isActive)
                .findFirst();
    }

    private Optional<MultistageAbstractCommand> getReadyCommand() {
        return activeCommands
                .stream()
                .filter(command -> command.isReady() && !command.isActive())
                .findFirst();
    }

    private Command getSimpleCommand(String s) {
        return commands.getOrDefault(s, defaultCommand);
    }

    @Autowired
    public void setSipmleCommands(List<Command> simpleCommands) {
        this.commands = simpleCommands
                .stream()
                .collect(toMap(Command::getName, identity()));
    }
}
