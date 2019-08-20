package com.telega.bot.memebot.invokers;

import com.telega.bot.memebot.commands.abstracts.MultistageAbstractCommand;
import com.telega.bot.memebot.commands.interfaces.Command;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CommandInvoker {

    public void invokeSimpleCommand(Command simpleCommand, Update update) {
        simpleCommand.execute(update);
    }

    public void initMultistageCommand(MultistageAbstractCommand command, Update update) {
        command.init(update);
    }

    public void executeMultistageCommand(MultistageAbstractCommand command, Update update) {
        command.execute(update);
    }

}
