package com.telega.bot.memebot.commands.impl.simple;

import com.telega.bot.memebot.bots.PollingTelegramBot;
import com.telega.bot.memebot.commands.interfaces.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class YouAreNotMySenpaiCommand implements Command {

    public static final String NAME = "/senpai";
    private static final String INCOGNITO_TEXT = "I don't know you!";

    private final PollingTelegramBot pollingTelegramBot;

    @Value("classpath:stickers/senpai.webp")
    private Resource resource;

    private SendSticker createSendSticker(Long chatId){
        SendSticker sticker = new SendSticker();
        try {
            sticker.setSticker(new InputFile(resource.getFile(), "senpai"));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        sticker.setChatId(chatId);
        return sticker;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        SendSticker sendSticker = createSendSticker(chatId);
        pollingTelegramBot.sendSticker(sendSticker);
        pollingTelegramBot.sendMessage(INCOGNITO_TEXT, chatId);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return "senpai";
    }
}
