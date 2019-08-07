package com.telega.bot.memebot.commands.impl.sipmle;

import com.telega.bot.memebot.bots.PollingTelegramBot;
import com.telega.bot.memebot.commands.interfaces.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class GreetCommand implements Command {

	private final PollingTelegramBot pollingTelegramBot;

	@Override
	public void execute(Update update) {
		pollingTelegramBot.sendMessage("Hello", update.getMessage().getChatId());
	}

	@Override
	public String getName() {
		return "/greet";
	}

	@Override
	public String getDescription() {
		return "Greets the user";
	}
}
