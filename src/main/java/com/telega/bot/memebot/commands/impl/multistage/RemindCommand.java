package com.telega.bot.memebot.commands.impl.multistage;

import com.telega.bot.memebot.annotations.BotCommand;
import com.telega.bot.memebot.bots.PollingTelegramBot;
import com.telega.bot.memebot.commands.abstracts.MultistageAbstractCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ScheduledExecutorService;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.parse;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
@Component
@BotCommand
public class RemindCommand extends MultistageAbstractCommand {

    private static final String DATE_FORMATTER = "yyyy-MM-dd HH:mm";

    private ScheduledExecutorService executorService;

    public RemindCommand(PollingTelegramBot pollingTelegramBot) {
        super(pollingTelegramBot);
    }

    @Override
    public void initSteps() {
        addStep("Enter what you need to be reminded");
        addStep("Enter the date in format: " + DATE_FORMATTER);
    }

    @Override
    public void initAnswers() {
        addAnswer("text");
        addAnswer("date");
    }

    @Override
    public void execute(Update update) {
        LocalDateTime date = toLocalDateTime(getMap().get("date"));
		Long chatId = update.getMessage().getChatId();
		if (date == null || date.isBefore(now())) {
			pollingTelegramBot.sendMessage("Date is not appropriate!", chatId);
            return;
        }
        String text = getMap().get("text");
        createTask(date, text, chatId);
        destroy();
    }

    private void createTask(LocalDateTime date, String text, Long chatId) {
        Runnable task = () -> pollingTelegramBot.sendMessage(text,chatId);
        executorService.schedule(task, now().until(date, MILLIS), MILLISECONDS);
    }

    private LocalDateTime toLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        LocalDateTime formatDateTime = null;
        try {
            formatDateTime = parse(date, formatter);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return formatDateTime;
    }


    @Override
    public String getName() {
        return "/remind";
    }

    @Override
    public String getDescription() {
        return "Reminds what you need to do";
    }

    @Autowired
    public void setExecutorService(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }
}
