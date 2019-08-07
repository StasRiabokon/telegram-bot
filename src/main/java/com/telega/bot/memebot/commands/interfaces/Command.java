package com.telega.bot.memebot.commands.interfaces;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {

	void execute(Update update);

	default String getName(){

		return "default";
	}
	default String getDescription(){
		return "default";
	}

}
