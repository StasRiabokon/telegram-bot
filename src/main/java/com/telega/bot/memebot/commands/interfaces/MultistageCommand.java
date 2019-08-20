package com.telega.bot.memebot.commands.interfaces;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MultistageCommand extends Command {

    void init(Update update);

    void destroy();

}
