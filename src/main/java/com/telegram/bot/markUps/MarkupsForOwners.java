package com.telegram.bot.markUps;

import com.telegram.bot.models.Animal;
import com.telegram.bot.models.Shelters;
import com.telegram.bot.models.Volunteers;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class MarkupsForOwners {

    public static InlineKeyboardMarkup variantsForAdded(long chat_id) {
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

        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();

        InlineKeyboardButton putAnimal = new InlineKeyboardButton();
        putAnimal.setText("Отдать животное");
        putAnimal.setCallbackData("put_animal " + chat_id);



        rowInline.add(addShelters);
        rowInline2.add(addAnimal);
        rowInline3.add(putAnimal);

        rowsInlineKeyboardButtons.add(rowInline);
        rowsInlineKeyboardButtons.add(rowInline2);
        rowsInlineKeyboardButtons.add(rowInline3);

        inlineKeyboardMarkup.setKeyboard(rowsInlineKeyboardButtons);

        return inlineKeyboardMarkup;

    }

    public static InlineKeyboardMarkup variantsForKind(long chat_id) {
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

    public static InlineKeyboardMarkup allShelters(long chat_id, int messageId, int page, List<Shelters> shelters) {
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

    public static InlineKeyboardMarkup allSheltersForPutAnimal(long chat_id, int messageId, int page, List<Shelters> shelters) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlineKeyboardButtons = new ArrayList<>();

        for (Shelters shelter : shelters) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            InlineKeyboardButton button = new InlineKeyboardButton();

            button.setText(shelter.getKind().toUpperCase() + " " + shelter.getName());

            button.setCallbackData("click_on_shelter_put_animal " + chat_id + " " + shelter.getId());

            rowInline.add(button);
            rowsInlineKeyboardButtons.add(rowInline);

        }

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton next = new InlineKeyboardButton();

        next.setText("->");
        next.setCallbackData("next_shelter_put_animal " + (page + 1) + " " + chat_id + " " + messageId);

        InlineKeyboardButton counter = new InlineKeyboardButton();
        counter.setText(page + "");
        counter.setCallbackData("counter ");

        InlineKeyboardButton prev = new InlineKeyboardButton();
        prev.setText("<-");
        prev.setCallbackData("prev_shelter_put_animal " + (page - 1) + " " + chat_id + " " + messageId);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Назад");
        back.setCallbackData("back_shelter_put_animal " + chat_id + " " + messageId);

        rowInline.add(prev);
        rowInline.add(counter);
        rowInline.add(next);
        rowInline2.add(back);

        rowsInlineKeyboardButtons.add(rowInline);
        rowsInlineKeyboardButtons.add(rowInline2);

        inlineKeyboardMarkup.setKeyboard(rowsInlineKeyboardButtons);

        return inlineKeyboardMarkup;


    }

    public static InlineKeyboardMarkup allAnimalsForPutAnimal(long chat_id, int messageId, int page, List<Animal> animals,
                                                              long shelter_id) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlineKeyboardButtons = new ArrayList<>();
        for (Animal animal : animals) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            InlineKeyboardButton button = new InlineKeyboardButton();

            button.setText(animal.getKind().toUpperCase() + " " + animal.getColor());

            button.setCallbackData("click_on_animal_put_animal " + chat_id + " " + animal.getId() +
                    " " + shelter_id);

            rowInline.add(button);
            rowsInlineKeyboardButtons.add(rowInline);

        }

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton next = new InlineKeyboardButton();

        next.setText("->");
        next.setCallbackData("next_animal_put_animal " + (page + 1) + " " + chat_id + " " + messageId + " " + shelter_id);

        InlineKeyboardButton counter = new InlineKeyboardButton();
        counter.setText(page + "");
        counter.setCallbackData("counter ");

        InlineKeyboardButton prev = new InlineKeyboardButton();
        prev.setText("<-");
        prev.setCallbackData("prev_animal_put_animal " + (page - 1) + " " + chat_id + " " + messageId + " " + shelter_id);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();

        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Назад");
        back.setCallbackData("back_animal_put_animal " + chat_id + " " + messageId + " " + shelter_id);

        rowInline.add(prev);
        rowInline.add(counter);
        rowInline.add(next);
        rowInline2.add(back);

        rowsInlineKeyboardButtons.add(rowInline);
        rowsInlineKeyboardButtons.add(rowInline2);

        inlineKeyboardMarkup.setKeyboard(rowsInlineKeyboardButtons);

        return inlineKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup acceptPhoto() {
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

    public static InlineKeyboardMarkup VolunteersForPutAnimal(long chat_id, int messageId, int page,
                                                              List<Volunteers> volunteers,
                                                             long shelter_id) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlineKeyboardButtons = new ArrayList<>();

        Collections.shuffle(volunteers);

        for (Volunteers volunteer : volunteers) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            InlineKeyboardButton button = new InlineKeyboardButton();

            button.setText(volunteer.getName());

            button.setCallbackData("click_on_volunteer_put_animal " + chat_id + " " + volunteer.getId() +
                    " " + shelter_id);

            rowInline.add(button);
            rowsInlineKeyboardButtons.add(rowInline);

        }

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton next = new InlineKeyboardButton();

        next.setText("->");
        next.setCallbackData("next_volunteer_put_animal " + (page + 1) + " " + chat_id + " " + messageId + " " + shelter_id);

        InlineKeyboardButton counter = new InlineKeyboardButton();
        counter.setText(page + "");
        counter.setCallbackData("counter ");

        InlineKeyboardButton prev = new InlineKeyboardButton();
        prev.setText("<-");
        prev.setCallbackData("prev_volunteer_put_animal " + (page - 1) + " " + chat_id + " " + messageId + " " + shelter_id);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();

        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Назад");
        back.setCallbackData("back_volunteer_put_animal " + chat_id + " " + messageId + " " + shelter_id);

        rowInline.add(prev);
        rowInline.add(counter);
        rowInline.add(next);
        rowInline2.add(back);

        rowsInlineKeyboardButtons.add(rowInline);
        rowsInlineKeyboardButtons.add(rowInline2);

        inlineKeyboardMarkup.setKeyboard(rowsInlineKeyboardButtons);

        return inlineKeyboardMarkup;

    }

}
