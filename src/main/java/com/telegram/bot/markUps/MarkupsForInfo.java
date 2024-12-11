package com.telegram.bot.markUps;

import com.telegram.bot.models.Animal;
import com.telegram.bot.models.Shelters;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class MarkupsForInfo {

    public static InlineKeyboardMarkup getInfoAboutShelters(long chat_id) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlineKeyboardButtons = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();


        InlineKeyboardButton info = new InlineKeyboardButton();
        info.setText("Посмотреть свои приюты");
        info.setCallbackData("getInfoAboutShelters " + chat_id);

        rowInline.add(info);
        rowsInlineKeyboardButtons.add(rowInline);
        inlineKeyboardMarkup.setKeyboard(rowsInlineKeyboardButtons);

        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getInfoShelters(long chat_id, int messageId, int page, List<Shelters> shelters) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlineKeyboardButtons = new ArrayList<>();

        for (Shelters shelter : shelters) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            InlineKeyboardButton button = new InlineKeyboardButton();

            button.setText(shelter.getKind().toUpperCase() + " " + shelter.getName());

            button.setCallbackData("info_on_shelter " + chat_id + " " + shelter.getId());

            rowInline.add(button);
            rowsInlineKeyboardButtons.add(rowInline);

        }

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();

        InlineKeyboardButton next = new InlineKeyboardButton();

        next.setText("->");
        next.setCallbackData("next_info_shelter " + (page + 1) + " " + chat_id + " " + messageId);

        InlineKeyboardButton counter = new InlineKeyboardButton();
        counter.setText(page + "");
        counter.setCallbackData("counter ");

        InlineKeyboardButton prev = new InlineKeyboardButton();
        prev.setText("<-");
        prev.setCallbackData("prev_info_shelter " + (page - 1) + " " + chat_id + " " + messageId);

        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Назад");
        back.setCallbackData("back_to_profile " + chat_id + " " + messageId);

        rowInline.add(prev);
        rowInline.add(counter);
        rowInline.add(next);

        rowInline2.add(back);

        rowsInlineKeyboardButtons.add(rowInline);
        rowsInlineKeyboardButtons.add(rowInline2);

        inlineKeyboardMarkup.setKeyboard(rowsInlineKeyboardButtons);

        return inlineKeyboardMarkup;

    }

    public static InlineKeyboardMarkup getInfoAllShelters(long chat_id, int messageId, int page, List<Shelters> shelters,
                                                       boolean backButton) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlineKeyboardButtons = new ArrayList<>();

        for (Shelters shelter : shelters) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            InlineKeyboardButton button = new InlineKeyboardButton();

            button.setText(shelter.getKind().toUpperCase() + " "  +
                    shelter.getOwnerShelters().stream().toList().get(0).getName() + " " + shelter.getName());

            button.setCallbackData("info_on_shelter_user " + chat_id + " " + shelter.getId());

            rowInline.add(button);
            rowsInlineKeyboardButtons.add(rowInline);

        }

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton next = new InlineKeyboardButton();

        next.setText("->");
        next.setCallbackData("next_info_shelter_user " + (page + 1) + " " + chat_id + " " + messageId);

        InlineKeyboardButton counter = new InlineKeyboardButton();
        counter.setText(page + "");
        counter.setCallbackData("counter ");

        InlineKeyboardButton prev = new InlineKeyboardButton();
        prev.setText("<-");
        prev.setCallbackData("prev_info_shelter_user " + (page - 1) + " " + chat_id + " " + messageId);


        rowInline.add(prev);
        rowInline.add(counter);
        rowInline.add(next);


        rowsInlineKeyboardButtons.add(rowInline);

        if (backButton) {
            List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
            InlineKeyboardButton back = new InlineKeyboardButton();
            back.setText("Назад");
            back.setCallbackData("back_to_profile_user " + chat_id + " " + messageId);
            rowInline2.add(back);
            rowsInlineKeyboardButtons.add(rowInline2);
        }

        inlineKeyboardMarkup.setKeyboard(rowsInlineKeyboardButtons);

        return inlineKeyboardMarkup;

    }

    public static InlineKeyboardMarkup getAnimals(long chat_id, int messageId,
                                                  int page, List<Animal> animals,
                                                  long shelter_id) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlineKeyboardButtons = new ArrayList<>();

        for (Animal animal : animals) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            InlineKeyboardButton button = new InlineKeyboardButton();

            button.setText(animal.getKind().toUpperCase() + " " + animal.getColor());

            button.setCallbackData("info_about_animal " + chat_id + " " + animal.getId() + " " + shelter_id);

            rowInline.add(button);
            rowsInlineKeyboardButtons.add(rowInline);

        }

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();

        InlineKeyboardButton next = new InlineKeyboardButton();

        next.setText("->");
        next.setCallbackData("next_info_animal_user " + (page + 1) + " " + chat_id + " " + messageId + " " + shelter_id);

        InlineKeyboardButton counter = new InlineKeyboardButton();
        counter.setText(page + "");
        counter.setCallbackData("counter ");

        InlineKeyboardButton prev = new InlineKeyboardButton();
        prev.setText("<-");
        prev.setCallbackData("prev_info_animal_user " + (page - 1) + " " + chat_id + " " + messageId + " " + shelter_id);

        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Назад");
        back.setCallbackData("back_to_shelters " + chat_id + " " + messageId);

        rowInline.add(prev);
        rowInline.add(counter);
        rowInline.add(next);

        rowInline2.add(back);

        rowsInlineKeyboardButtons.add(rowInline);
        rowsInlineKeyboardButtons.add(rowInline2);

        inlineKeyboardMarkup.setKeyboard(rowsInlineKeyboardButtons);

        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getAnimalsUser(long chat_id, int messageId,
                                                  int page, List<Animal> animals,
                                                  long shelter_id) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlineKeyboardButtons = new ArrayList<>();

        for (Animal animal : animals) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            InlineKeyboardButton button = new InlineKeyboardButton();

            button.setText(animal.getKind().toUpperCase() + " " + animal.getColor());

            button.setCallbackData("info_about_animal_user " + chat_id + " " + animal.getId() + " " + shelter_id);

            rowInline.add(button);
            rowsInlineKeyboardButtons.add(rowInline);

        }

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();

        InlineKeyboardButton next = new InlineKeyboardButton();

        next.setText("->");
        next.setCallbackData("next_info_animal_user " + (page + 1) + " " + chat_id + " " + messageId + " " + shelter_id);

        InlineKeyboardButton counter = new InlineKeyboardButton();
        counter.setText(page + "");
        counter.setCallbackData("counter ");

        InlineKeyboardButton prev = new InlineKeyboardButton();
        prev.setText("<-");
        prev.setCallbackData("prev_info_animal_user " + (page - 1) + " " + chat_id + " " + messageId + " " + shelter_id);

        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Назад");
        back.setCallbackData("back_to_shelters_user " + chat_id + " " + messageId);

        rowInline.add(prev);
        rowInline.add(counter);
        rowInline.add(next);

        rowInline2.add(back);

        rowsInlineKeyboardButtons.add(rowInline);
        rowsInlineKeyboardButtons.add(rowInline2);

        inlineKeyboardMarkup.setKeyboard(rowsInlineKeyboardButtons);

        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup photoBack(long chat_id, long shelter_id) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlineKeyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Назад");
        back.setCallbackData("back_from_animals " + chat_id + " " + shelter_id);

        rowInline.add(back);
        rowsInlineKeyboardButtons.add(rowInline);
        inlineKeyboardMarkup.setKeyboard(rowsInlineKeyboardButtons);

        return inlineKeyboardMarkup;
    }
}
