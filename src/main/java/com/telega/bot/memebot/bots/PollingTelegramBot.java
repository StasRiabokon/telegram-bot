package com.telega.bot.memebot.bots;

import com.telega.bot.memebot.resolvers.CommandResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@RequiredArgsConstructor
public class PollingTelegramBot extends TelegramLongPollingBot {

	@Value("${bot.api.name}")
	private String botUsername;

	@Value("${bot.api.token}")
	private String botToken;

	private CommandResolver commandResolver;

	@Override
	public void onUpdateReceived(Update update) {
		commandResolver.resolve(update);
	}

	@Override
	public String getBotUsername() {
		return botUsername;
	}

	@Override
	public String getBotToken() {
		return botToken;
	}

	public void sendMessage(String content, Long idChat) {
		sendMessage(content, idChat, false);
	}

	public void sendMessage(String content, Long idChat, boolean html) {
		SendMessage message = new SendMessage()
				.setChatId(idChat)
				.setText(content)
				.enableHtml(html);
		try {
			this.execute(message);
		} catch (TelegramApiException e) {
			log.error(e.getMessage(), e);
		}
	}

	@Lazy
	@Autowired
	public void setCommandResolver(CommandResolver commandResolver) {
		this.commandResolver = commandResolver;
	}

}
