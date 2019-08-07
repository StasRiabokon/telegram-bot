package com.telega.bot.memebot.models;

import lombok.Data;

@Data
public class Currency {

	private String baseCurrency;
	private String currency;
	private Double saleRate;
	private Double purchaseRate;

}
