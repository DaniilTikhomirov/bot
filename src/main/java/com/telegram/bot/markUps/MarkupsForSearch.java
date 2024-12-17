package com.telegram.bot.markUps;

import com.telegram.bot.models.Shelters;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class MarkupsForSearch {

    public static InlineKeyboardMarkup searchShelter(long chat_id, int messageId, int page, List<Shelters> shelters){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlineKeyboardButtons = new ArrayList<>();

        for (Shelters shelter : shelters) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            InlineKeyboardButton button = new InlineKeyboardButton();

            button.setText(shelter.getKind().toUpperCase() + " " + shelter.getName());

            button.setCallbackData("choose_shelter " + chat_id + " " + shelter.getId());

            rowInline.add(button);
            rowsInlineKeyboardButtons.add(rowInline);

        }

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton next = new InlineKeyboardButton();

        next.setText("->");
        next.setCallbackData("next_search_shelter " + (page + 1) + " " + chat_id + " " + messageId);

        InlineKeyboardButton counter = new InlineKeyboardButton();
        counter.setText(page + "");
        counter.setCallbackData("counter ");

        InlineKeyboardButton prev = new InlineKeyboardButton();
        prev.setText("<-");
        prev.setCallbackData("prev_search_shelter " + (page - 1) + " " + chat_id + " " + messageId);

        rowInline.add(prev);
        rowInline.add(counter);
        rowInline.add(next);


        rowsInlineKeyboardButtons.add(rowInline);

        inlineKeyboardMarkup.setKeyboard(rowsInlineKeyboardButtons);

        return inlineKeyboardMarkup;

    }

}
