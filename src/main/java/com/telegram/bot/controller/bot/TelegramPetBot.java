package com.telegram.bot.controller.bot;

import com.telegram.bot.config.BotConfig;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


/**
 * Телеграм-бот для взаимодействия с пользователями, представляющими приют для животных.
 *
 * <p>Этот класс реализует {@link TelegramLongPollingBot} для обработки входящих обновлений от пользователей в Telegram.</p>
 *
 * <p>Класс использует конфигурацию, полученную из {@link BotConfig}, для получения токена бота и имени бота.</p>
 *
 * <p>Методы:
 * <ul>
 *     <li>{@link #onUpdateReceived(Update)} - обрабатывает входящие сообщения от пользователей;</li>
 *     <li>{@link #getBotUsername()} - возвращает имя бота;</li>
 *     <li>{@link #PutMessage(long, String)} - отправляет сообщение пользователю;</li>
 *     <li>{@link #StartMessage(long, String)} - отправляет приветственное сообщение при старте взаимодействия.</li>
 * </ul>
 * </p>
 *
 * <p>В классе скрыт Swagger UI с помощью аннотации {@link Hidden}.</p>
 **/
@Slf4j
@Hidden
@Component
public class TelegramPetBot extends TelegramLongPollingBot {

    private final BotConfig config;

    public TelegramPetBot(BotConfig config) {
        super(config.getBotToken());
        this.config = config;
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (message.equals("/start")) {
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
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }


    }

    private void StartMessage(long chatId, String name) {
        String answer = "Привет " + name;
        PutMessage(chatId, answer);
    }
}
