package com.telegram.bot.callBack;

import com.telegram.bot.markUps.MarkupsForInfo;
import com.telegram.bot.markUps.MarkupsForOwners;
import com.telegram.bot.markUps.MarkupsForSearch;
import com.telegram.bot.models.Animal;
import com.telegram.bot.models.Shelters;
import com.telegram.bot.models.Volunteers;
import com.telegram.bot.received.TelegramAdmins;
import com.telegram.bot.received.TelegramOwners;
import com.telegram.bot.received.TelegramUsers;
import com.telegram.bot.received.TelegramVolunteers;
import com.telegram.bot.services.OwnerSheltersService;
import com.telegram.bot.services.SheltersService;
import com.telegram.bot.telegram_utils.MessageProvider;
import com.telegram.bot.telegram_utils.ModelsHelper;
import com.telegram.bot.telegram_utils.StatesStorage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;


@Component
public class CallBack {
    private final TelegramAdmins telegramAdmins;
    private final TelegramOwners telegramOwners;
    private final OwnerSheltersService ownerSheltersService;
    private final MarkupsForOwners markupsForOwners;
    private final MessageProvider messageProvider;
    private final SheltersService sheltersService;
    private final TelegramUsers telegramUsers;
    private final StatesStorage statesStorage;
    private final TelegramVolunteers telegramVolunteers;


    public CallBack(TelegramAdmins telegramAdmins,
                    TelegramOwners telegramOwners,
                    OwnerSheltersService ownerSheltersService,
                    MarkupsForOwners markupsForOwners,
                    MessageProvider messageProvider, SheltersService sheltersService, TelegramUsers telegramUsers, StatesStorage statesStorage, TelegramVolunteers telegramVolunteers) {
        this.telegramAdmins = telegramAdmins;
        this.telegramOwners = telegramOwners;
        this.ownerSheltersService = ownerSheltersService;
        this.markupsForOwners = markupsForOwners;
        this.messageProvider = messageProvider;
        this.sheltersService = sheltersService;
        this.telegramUsers = telegramUsers;
        this.statesStorage = statesStorage;
        this.telegramVolunteers = telegramVolunteers;
    }


    public void callBackReaction(Update update) {
        String call_data = update.getCallbackQuery().getData();
        int messageId = update.getCallbackQuery().getMessage().getMessageId();

        long chat_id = update.getCallbackQuery().getMessage().getChatId();
        int page = 1;
        int size = 5;

        String[] call_split_data = call_data.split(" ");
        System.out.println(Arrays.toString(call_split_data));
        if (call_split_data.length == 3) {
            telegramAdmins.administratorCallBack(chat_id, call_split_data);
            telegramOwners.ownerCallBack(chat_id, call_split_data, messageId, page, size);
            telegramUsers.userCallBack(chat_id, call_split_data, messageId, page, size);
            telegramVolunteers.volunteersCallBack(chat_id, call_split_data, messageId, page, size);
        } else if (call_split_data.length == 2) {
            telegramOwners.ownerCallBack(chat_id, call_split_data, messageId, page, size);
        } else if (call_split_data.length >= 4) {
            sheltersNextPrevCallBack(call_split_data, messageId, size);
            telegramOwners.ownerCallBack(chat_id, call_split_data, messageId, page, size);
            telegramUsers.userCallBack(chat_id, call_split_data, messageId, page, size);
        }
    }

    private void sheltersNextPrevCallBack(String[] call_split_data, int messageId, int size) {
        if (call_split_data[0].startsWith("next") || call_split_data[0].startsWith("prev")) {
            int page = Integer.parseInt(call_split_data[1]);
            long chat_id = Long.parseLong(call_split_data[2]);
            String data = call_split_data[0];

            if (page < 1) {
                return;
            }

            switch (data) {
                case "next_shelter", "prev_shelter" -> {
                    List<Shelters> shelters = ownerSheltersService.getPageShelters(page, size, chat_id);
                    if (shelters.isEmpty()) {
                        return;
                    }
                    messageProvider.changeInline(chat_id, messageId, MarkupsForOwners.allShelters(chat_id, messageId, page, shelters));
                }
                case "next_info_shelter", "prev_info_shelter" -> {
                    List<Shelters> shelters = ownerSheltersService.getPageShelters(page, size, chat_id);
                    if (shelters.isEmpty()) {
                        return;
                    }

                    messageProvider.changeInline(chat_id, messageId,
                            MarkupsForInfo.getInfoShelters(chat_id, messageId, page, shelters));
                }
                case "next_info_animal", "prev_info_animal" -> {
                    List<Animal> animals = sheltersService.getPageAnimals(page, size, Long.parseLong(call_split_data[4]),
                            true);
                    if (animals.isEmpty()) {
                        return;
                    }

                    messageProvider.changeInline(chat_id, messageId,
                            MarkupsForInfo.getAnimals(chat_id, messageId, page, animals,
                                    Long.parseLong(call_split_data[4])));
                }
                case "next_info_shelter_user", "prev_info_shelter_user" -> {
                    List<Shelters> shelters = sheltersService.getPageAllShelters(page, size);

                    if (shelters.isEmpty()) {
                        return;
                    }

                    messageProvider.changeInline(chat_id, messageId,
                            MarkupsForInfo.getInfoAllShelters(chat_id, messageId,
                                    page, shelters, false));
                }
                case "next_info_animal_user", "prev_info_animal_user" -> {
                    List<Animal> animals = sheltersService.getPageAnimals(page, size, Long.parseLong(call_split_data[4]),
                            true);
                    if (animals.isEmpty()) {
                        return;
                    }

                    messageProvider.changeInline(chat_id, messageId,
                            MarkupsForInfo.getAnimalsUser(chat_id, messageId, page, animals,
                                    Long.parseLong(call_split_data[4])));

                }case "next_search_shelter", "prev_search_shelter" -> {
                    List<Shelters> shelters = statesStorage.getSheltersForSearch().get(chat_id);
                    List<Shelters> sheltersPage = ModelsHelper.getPage(page, size, shelters);


                    if (sheltersPage.isEmpty()) {
                        return;
                    }

                    messageProvider.changeInline(chat_id, messageId,
                            MarkupsForSearch.searchShelter(chat_id, messageId, page, sheltersPage));

                }case "next_animal_put_animal", "prev_animal_put_animal" -> {
                    List<Animal> animals = sheltersService.getPageAnimals(page, size,
                            Long.parseLong(call_split_data[4]), true);

                    if (animals.isEmpty()){
                        return;
                    }


                    messageProvider.changeInline(chat_id, messageId, MarkupsForOwners.allAnimalsForPutAnimal(chat_id,
                            messageId, page, animals, Long.parseLong(call_split_data[4])));
                }case "next_volunteer_put_animal", "prev_volunteer_put_animal" -> {
                    List<Volunteers> volunteers = sheltersService.
                            getShelterById(Long.parseLong(call_split_data[4])).getVolunteers().stream().toList();

                    List<Volunteers> volunteersPage = ModelsHelper.getPage(page, size, volunteers);

                    if (volunteersPage.isEmpty()) {
                        return;
                    }

                    messageProvider.changeInline(chat_id, messageId,
                            MarkupsForOwners.VolunteersForPutAnimal(chat_id, messageId, page, volunteersPage,
                                    Long.parseLong(call_split_data[4])));
                }
            }
        }
    }
}
