package com.telegram.bot.markUps;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class MarkupsForAdmins {

    /**
     * Создает клавиатуру для обработки заявок администратором.
     *
     * <p>Кнопки позволяют принять или отклонить заявку.</p>
     *
     * @param chat_id идентификатор чата заявителя.
     * @param counter уникальный счетчик для отслеживания заявок.
     * @return объект {@link InlineKeyboardMarkup} с кнопками.
     **/
    public InlineKeyboardMarkup rejectAcceptMarkUp(long chat_id, int counter, String rejectQ, String acceptQ) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlineKeyboardButtons = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton accept = new InlineKeyboardButton();
        accept.setText("Принять");
        accept.setCallbackData(acceptQ + " " + chat_id + " " + counter);

        InlineKeyboardButton reject = new InlineKeyboardButton();
        reject.setText("Отклонить");
        reject.setCallbackData(rejectQ + " " + chat_id + " " + counter);

        rowInline.add(accept);
        rowInline.add(reject);
        rowsInlineKeyboardButtons.add(rowInline);

        inlineKeyboardMarkup.setKeyboard(rowsInlineKeyboardButtons);

        return inlineKeyboardMarkup;
    }
}
