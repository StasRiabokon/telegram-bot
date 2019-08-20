package com.telega.bot.memebot.commands.impl.multistage;

import com.telega.bot.memebot.bots.PollingTelegramBot;
import com.telega.bot.memebot.commands.abstracts.MultistageAbstractCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class RemindCommand extends MultistageAbstractCommand {

	public RemindCommand(PollingTelegramBot pollingTelegramBot) {
		super(pollingTelegramBot);
	}

	@Override
	public void initSteps() {
		addStep("Enter what you need to be reminded");
		addStep("Enter the date");
	}

	@Override
	public void initAnswers() {
		addAnswer("text");
		addAnswer("date");
	}

	@Override
	public void execute(Update update) {
		pollingTelegramBot.sendMessage(getMap().toString(), update.getMessage().getChatId());
		destroy();
	}

	@Override
	public String getName() {
		return "/remind";
	}

	@Override
	public String getDescription() {
		return "Reminds what you need to do";
	}
}
