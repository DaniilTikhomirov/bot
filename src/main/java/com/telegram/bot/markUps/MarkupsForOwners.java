package com.telegram.bot.markUps;

import com.telegram.bot.models.Shelters;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class MarkupsForOwners {

    public InlineKeyboardMarkup variantsForAdded(long chat_id) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlineKeyboardButtons = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton addShelters = new InlineKeyboardButton();
        addShelters.setText("Добавить Приют");
        addShelters.setCallbackData("add_shelters " + chat_id);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();

        InlineKeyboardButton addAnimal = new InlineKeyboardButton();
        addAnimal.setText("добавить животное");
        addAnimal.setCallbackData("add_animal " + chat_id);

        rowInline.add(addShelters);
        rowInline2.add(addAnimal);

        rowsInlineKeyboardButtons.add(rowInline);
        rowsInlineKeyboardButtons.add(rowInline2);

        inlineKeyboardMarkup.setKeyboard(rowsInlineKeyboardButtons);

        return inlineKeyboardMarkup;

    }

    public InlineKeyboardMarkup variantsForKind(long chat_id) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlineKeyboardButtons = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton cat = new InlineKeyboardButton();
        cat.setText("Приют Кошек");
        cat.setCallbackData("cat " + chat_id);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();

        InlineKeyboardButton dog = new InlineKeyboardButton();
        dog.setText("Приют Собак");
        dog.setCallbackData("dog " + chat_id);

        rowInline.add(cat);
        rowInline2.add(dog);

        rowsInlineKeyboardButtons.add(rowInline);
        rowsInlineKeyboardButtons.add(rowInline2);

        inlineKeyboardMarkup.setKeyboard(rowsInlineKeyboardButtons);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup allShelters(long chat_id, int messageId, int page, List<Shelters> shelters) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlineKeyboardButtons = new ArrayList<>();

        for (Shelters shelter : shelters) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            InlineKeyboardButton button = new InlineKeyboardButton();

            button.setText(shelter.getKind().toUpperCase() + " " + shelter.getName());

            button.setCallbackData("click_on_shelter " + chat_id + " " + shelter.getId());

            rowInline.add(button);
            rowsInlineKeyboardButtons.add(rowInline);

        }

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton next = new InlineKeyboardButton();

        next.setText("->");
        next.setCallbackData("next_shelter " + (page + 1) + " " + chat_id + " " + messageId);

        InlineKeyboardButton counter = new InlineKeyboardButton();
        counter.setText(page + "");
        counter.setCallbackData("counter ");

        InlineKeyboardButton prev = new InlineKeyboardButton();
        prev.setText("<-");
        prev.setCallbackData("prev_shelter " + (page - 1) + " " + chat_id + " " + messageId);

        rowInline.add(prev);
        rowInline.add(counter);
        rowInline.add(next);

        rowsInlineKeyboardButtons.add(rowInline);

        inlineKeyboardMarkup.setKeyboard(rowsInlineKeyboardButtons);

        return inlineKeyboardMarkup;


    }

    public ReplyKeyboardMarkup acceptPhoto() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow acceptRow = new KeyboardRow();
        acceptRow.add("сохранить фотографии");
        keyboardRows.add(acceptRow);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;

    }
}
