package com.telega.bot.memebot.commands.abstracts;

import com.telega.bot.memebot.bots.PollingTelegramBot;
import com.telega.bot.memebot.commands.interfaces.MultistageCommand;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
@RequiredArgsConstructor
public abstract class MultistageAbstractCommand implements MultistageCommand {

    private boolean isReady;
    private boolean isActive;
    private Deque<String> steps = new ArrayDeque<>();
    private Deque<String> answers = new ArrayDeque<>();
    private Map<String, String> map = new HashMap<>();

    protected final PollingTelegramBot pollingTelegramBot;

    public abstract void initSteps();

    public abstract void initAnswers();

    @Override
    public void init(Update update) {
        isActive = true;
        String answer = update.getMessage().getText();
        if (!answer.equals(getName()) && hasAnswers()) {
            map.put(answers.pollLast(), answer);
        }
        if (hasSteps()) {
            pollingTelegramBot.sendMessage(getStep(), update.getMessage().getChatId());
        } else {
            isReady = true;
            isActive = false;
        }
    }

    @Override
    public void destroy() {
        isReady = false;
        isActive = false;
        map.clear();
    }

    public boolean hasSteps() {
        return !steps.isEmpty();
    }

    public boolean hasAnswers() {
        return !answers.isEmpty();
    }

    public String getStep() {
        return steps.pollLast();
    }

    public void addStep(String step) {
        steps.push(step);
    }

    public void addAnswer(String answer) {
        answers.push(answer);
    }


}
