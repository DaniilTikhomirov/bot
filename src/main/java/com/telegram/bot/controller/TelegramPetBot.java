package com.telegram.bot.controller;

import com.telegram.bot.config.BotConfig;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Hidden
@Component
public class TelegramPetBot extends TelegramLongPollingBot {

    BotConfig config;

    public TelegramPetBot(BotConfig config) {
        super(config.getBotToken());
        this.config = config;
    }


    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if(message.equals("/start")){
                StartMessage(chatId, update.getMessage().getChat().getUserName());
            }
        }
    }

    @Override
    public String getBotUsername() {
        return this.config.getBotName();
    }

    private void PutMessage(long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        }catch (TelegramApiException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }


    }

    private void StartMessage(long chatId, String name) {
        String answer = "Привет " + name;
        PutMessage(chatId, answer);
    }
}
