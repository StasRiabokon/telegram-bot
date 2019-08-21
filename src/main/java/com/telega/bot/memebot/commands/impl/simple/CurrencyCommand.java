package com.telega.bot.memebot.commands.impl.simple;

import com.telega.bot.memebot.bots.PollingTelegramBot;
import com.telega.bot.memebot.commands.interfaces.Command;
import com.telega.bot.memebot.models.Bank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CurrencyCommand implements Command {

	private final PollingTelegramBot pollingTelegramBot;
	private final RestTemplate restTemplate;

	@Value("${currency.api.url}")
	private String currencyApiUrl;

	@Override
	public void execute(Update update) {
		String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		String url = String.format(currencyApiUrl, date);
		Bank bank = restTemplate.getForObject(url, Bank.class);
		if (bank != null) {
			pollingTelegramBot.sendMessage(getCurrencyInfo(bank), update.getMessage().getChatId());
		}

	}

	private String getCurrencyInfo(Bank bank) {
		StringBuilder builder = new StringBuilder();
		builder
				.append("Date: ").append(bank.getDate()).append("\n")
				.append("Bank: ").append(bank.getBank()).append("\n");
		bank.getExchangeRate()
				.stream()
				.filter(currency -> Objects.nonNull(currency.getCurrency()))
				.filter(currency -> currency.getCurrency().equals("USD") ||
						currency.getCurrency().equals("EUR"))
				.forEach(currency -> builder.append(currency.getCurrency())
						.append(": ")
						.append(String.format("%.2f", currency.getPurchaseRate()))
						.append("/")
						.append(String.format("%.2f", currency.getSaleRate()))
						.append("\n"));
		return builder.toString();
	}

	@Override
	public String getName() {
		return "/currency";
	}

	@Override
	public String getDescription() {
		return "Shows the currency";
	}
}
