package com.telega.bot.memebot.commands.abstracts;

import com.telega.bot.memebot.bots.PollingTelegramBot;
import com.telega.bot.memebot.commands.interfaces.MultistageCommand;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Data
@Component
@RequiredArgsConstructor
public abstract class MultistageAbstractCommand implements MultistageCommand {

	private boolean isActive;
	private boolean isDone;
	private Deque<String> steps = new ArrayDeque<>();
	private List<String> answers = new ArrayList<>();

	private final PollingTelegramBot pollingTelegramBot;

	@PostConstruct
	public abstract void initSteps();

	@Override
	public void init() {
		isActive = true;
		isDone = false;
	}

	@Override
	public void execute(Update update) {
		String answer = update.getMessage().getText();
		if (!answer.equals(getName())) {
			answers.add(answer);
		}
		if (hasSteps()) {
			pollingTelegramBot.sendMessage(getStep(), update.getMessage().getChatId());
		} else {
			destroy();
		}
	}

	@Override
	public void destroy() {
		isActive = false;
		isDone = true;
		answers.clear();
	}

	public boolean hasSteps() {
		return !steps.isEmpty();
	}

	public String getStep() {
		return steps.pollLast();
	}

	public void addStep(String step) {
		steps.push(step);
	}


}
