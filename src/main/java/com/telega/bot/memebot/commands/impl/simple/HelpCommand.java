package com.telega.bot.memebot.commands.impl.simple;

import com.telega.bot.memebot.annotations.BotCommandBeanPostProcessor;
import com.telega.bot.memebot.bots.PollingTelegramBot;
import com.telega.bot.memebot.commands.interfaces.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {

	private final PollingTelegramBot pollingTelegramBot;

	private Map<String, String> commands;

	@Override
	public void execute(Update update) {
		String content = buildContent();
		pollingTelegramBot.sendMessage(content, update.getMessage().getChatId());
	}

	private String buildContent() {
		StringBuilder builder = new StringBuilder();
		builder.append("Available commands:\n");
		commands.remove("default");
		commands.forEach(
				(k, v) -> builder.append(k)
						.append(" - ")
						.append(v)
						.append("\n")
		);
		return builder.toString();
	}

	@EventListener(ContextRefreshedEvent.class)
	public void setCommands() {
		this.commands = BotCommandBeanPostProcessor.COMMANDS
				.stream()
				.collect(toMap(Command::getName, Command::getDescription));
	}

	@Override
	public String getName() {
		return "/help";
	}

	@Override
	public String getDescription() {
		return "Shows help";
	}
}
