package com.telegram.bot.telegram_utils;

import com.telegram.bot.config.BotConfig;
import com.telegram.bot.markUps.MarkupsForAdmins;
import com.telegram.bot.models.Administrator;
import com.telegram.bot.services.AdministratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@Slf4j
public class MessageProvider extends TelegramLongPollingBot {

    private final BotConfig config;

    private final AdministratorService administratorService;

    private final StatesStorage statesStorage;

    private final MarkupsForAdmins markupsForAdmins;

    public MessageProvider(BotConfig config,
                           AdministratorService administratorService,
                           StatesStorage statesStorage,
                           MarkupsForAdmins markupsForAdmins) {
        super(config.getBotToken());
        this.config = config;
        this.administratorService = administratorService;
        this.statesStorage = statesStorage;
        this.markupsForAdmins = markupsForAdmins;
    }

    /**
     * Отправляет текстовое сообщение пользователю.
     *
     * @param chatId  идентификатор чата Telegram.
     * @param message текст сообщения для отправки.
     * @throws RuntimeException если возникает ошибка API Telegram.
     */
    public void PutMessage(long chatId, String message) {
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

    /**
     * Отправляет заявку на регистрацию администраторам.
     *
     * <p>Сообщение включает данные заявителя и содержит клавиатуру для принятия или отклонения заявки.</p>
     *
     * @param chatId  идентификатор чата заявителя.
     * @param message текст заявки для отправки администраторам.
     */
    public void SendRegistrationToAdministrators(long chatId, String message,
                                                  String rejectQ, String acceptQ) {
        Map<Long, Integer> delMess = new ConcurrentHashMap<>();
        for (Administrator admin : administratorService.getAllAdministrators()) {
            PutMessageForAdmins(admin.getTelegramId(),
                    "заявка от " + chatId + "\n" + message,
                    markupsForAdmins.rejectAcceptMarkUp(chatId,
                            statesStorage.getCounterForDelMessage(), rejectQ, acceptQ), delMess);
        }

        statesStorage.delMessagesPut(statesStorage.getCounterForDelMessage(), delMess);
        statesStorage.incrementDelMessage();
    }

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    /**
     * Отправляет сообщение с клавиатурой администраторам.
     *
     * <p>Сообщение фиксируется для последующего удаления, чтобы избежать засорения чатов.</p>
     *
     * @param chatId               идентификатор чата администратора.
     * @param message              текст сообщения.
     * @param inlineKeyboardMarkup объект клавиатуры для сообщения.
     * @param delMess              карта для отслеживания отправленных сообщений.
     */
    public void PutMessageForAdmins(long chatId, String message,
                                     InlineKeyboardMarkup inlineKeyboardMarkup,
                                     Map<Long, Integer> delMess) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);


        try {
            Message SendOutMessage = execute(sendMessage);
            delMess.put(chatId, SendOutMessage.getMessageId());
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Отправляет сообщение с разметкой кнопок пользователю в Telegram.
     *
     * <p>Метод используется для отправки сообщения с прикрепленной клавиатурой
     * (например, для взаимодействия через кнопки).</p>
     *
     * @param chatId               идентификатор чата пользователя в Telegram.
     * @param message              текст сообщения, которое будет отправлено.
     * @param inlineKeyboardMarkup разметка кнопок, добавляемая к сообщению.
     */
    public void PutMessageWithMarkUp(long chatId, String message, ReplyKeyboard inlineKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void delAdminMessage(String[] call_split_data) {
        Map<Long, Integer> delMess = statesStorage.getDelMessages().get(Integer.parseInt(call_split_data[2]));

        for (Long key : delMess.keySet()) {
            delMessage(key, delMess.get(key));
        }
    }

    /**
     * Удаляет сообщение из чата Telegram.
     *
     * @param chatID    идентификатор чата Telegram.
     * @param messageId идентификатор сообщения для удаления.
     * @throws RuntimeException если возникает ошибка API Telegram.
     */
    public void delMessage(long chatID, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatID);
        deleteMessage.setMessageId(messageId);

        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void changeInline(long chatID, int messageID, InlineKeyboardMarkup keyboardMarkup) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setMessageId(messageID);
        editMessageReplyMarkup.setChatId(chatID);
        editMessageReplyMarkup.setReplyMarkup(keyboardMarkup);

        try {
            execute(editMessageReplyMarkup);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void changeText(long chatID, int messageID, String text){
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(messageID);
        editMessageText.setChatId(chatID);
        editMessageText.setText(text);

        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
