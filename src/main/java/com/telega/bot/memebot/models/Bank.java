package com.telega.bot.memebot.models;

import lombok.Data;

import java.util.List;

@Data
public class Bank {

	private String date;
	private String bank;
	private String baseCurrency;
	private List<Currency> exchangeRate;

}
