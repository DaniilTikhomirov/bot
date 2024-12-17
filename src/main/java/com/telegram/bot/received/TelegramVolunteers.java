package com.telegram.bot.received;

import com.telegram.bot.markUps.MarkupsForSearch;
import com.telegram.bot.models.Shelters;
import com.telegram.bot.models.Volunteers;
import com.telegram.bot.services.OwnerSheltersService;
import com.telegram.bot.services.SheltersService;
import com.telegram.bot.services.VolunteersService;
import com.telegram.bot.states.UserState;
import com.telegram.bot.telegram_utils.MessageProvider;
import com.telegram.bot.telegram_utils.ModelsHelper;
import com.telegram.bot.telegram_utils.StatesStorage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
public class TelegramVolunteers {

    private final StatesStorage statesStorage;
    private final MessageProvider messageProvider;
    private final SheltersService sheltersService;
    private final OwnerSheltersService ownerSheltersService;
    private final VolunteersService volunteersService;

    public TelegramVolunteers(StatesStorage statesStorage,
                              MessageProvider messageProvider,
                              SheltersService sheltersService,
                              OwnerSheltersService ownerSheltersService,
                              VolunteersService volunteersService) {
        this.statesStorage = statesStorage;
        this.messageProvider = messageProvider;
        this.sheltersService = sheltersService;
        this.ownerSheltersService = ownerSheltersService;
        this.volunteersService = volunteersService;
    }

    public void receivedStateVolunteers(long chat_id, String message, int messageID) {

        UserState state = statesStorage.getUserStates().get(chat_id);
        if (state == null){
            return;
        }

        if (message.startsWith("/")){
            return;
        }

        switch (state) {
            case registrationVolunteerName -> {handleRegisterName(chat_id, message);}
            case registrationVolunteerDescription -> {handleRegisterDescription(chat_id, message, messageID);}
            case registrationVolunteerSearchShelter -> {handleSearchShelter(chat_id, message, messageID);}
            case registrationVolunteerContact -> {handleRegisterContact(chat_id, message);}
            case registrationVolunteerWait -> {messageProvider.PutMessage(chat_id, "ожидайте ответа");}
        }
    }

    private void handleRegisterName(long chat_id, String message) {
        Volunteers volunteers = new Volunteers();
        volunteers.setName(message);

        statesStorage.registrationDataVolunteersPut(chat_id, volunteers);
        statesStorage.UserStatesPut(chat_id, UserState.registrationVolunteerDescription);

        messageProvider.PutMessage(chat_id, "опишите себя");

    }

    private void handleRegisterDescription(long chat_id, String message, int messageID) {
        Volunteers volunteers = statesStorage.getRegistrationDataVolunteers().get(chat_id);
        volunteers.setTelegramId(chat_id);
        volunteers.setDescription(message);

        statesStorage.registrationDataVolunteersPut(chat_id, volunteers);

        List<Shelters> shelters = sheltersService.getShelters().stream().toList();

        statesStorage.sheltersForSearchPut(chat_id, shelters);

        List<Shelters> sheltersPage = ModelsHelper.getPage(1, 5, shelters);

        statesStorage.UserStatesPut(chat_id, UserState.registrationVolunteerSearchShelter);

        Message mess = messageProvider.PutMessageWithMarkUp(chat_id, "выберете приют в которой отправить заявку." +
                " Для поиска просто напишите имя приюта. Чтобы показать все приюты напишите 'все'",
                MarkupsForSearch.searchShelter(chat_id, messageID, 1, sheltersPage));

        statesStorage.searchMessageIdPut(chat_id, mess.getMessageId());


    }

    private void handleSearchShelter(long chat_id, String message, int messageID) {
        List<Shelters> shelters;
        if(message.equalsIgnoreCase("все")){
            shelters = sheltersService.getShelters().stream().toList();
        }else {
            shelters = sheltersService.getSheltersByName(message);

        }

        statesStorage.sheltersForSearchPut(chat_id, shelters);
        List<Shelters> sheltersPage = ModelsHelper.getPage(1, 5, shelters);

        Message message1 = messageProvider.changeInline(chat_id, statesStorage.getSearchMessageId().get(chat_id),
                MarkupsForSearch.searchShelter(chat_id, messageID, 1, sheltersPage));

        statesStorage.searchMessageIdPut(chat_id, message1.getMessageId());

    }

    private void handleRegisterContact(long chat_id, String message) {
        Volunteers volunteers = statesStorage.getRegistrationDataVolunteers().get(chat_id);
        volunteers.setContact(message);

        statesStorage.registrationDataVolunteersPut(chat_id, volunteers);
        statesStorage.UserStatesPut(chat_id, UserState.registrationVolunteerWait);

        String info = String.format("имя: %s\nописание: %s\nконтакты: %s", volunteers.getName(),
                volunteers.getDescription(), volunteers.getContact());

        messageProvider.SendRegisterVolunteerToOwner(chat_id,
                volunteers.getShelters().getId(), info, "rejectV", "acceptV");

        messageProvider.PutMessage(chat_id, "ваше сообщение отправлено на рассмотрение");
    }

    public void volunteersCallBack(long chat_id, String[] call_split_data, int messageId, int page, int size){
        if(!volunteersService.isVolunteer(chat_id)){
            switch (call_split_data[0]){
                case "choose_shelter" -> {
                    Volunteers volunteers = statesStorage.getRegistrationDataVolunteers().get(chat_id);
                    volunteers.setShelters(sheltersService.getShelterById(Long.parseLong(call_split_data[2])));
                    statesStorage.registrationDataVolunteersPut(chat_id, volunteers);
                    statesStorage.UserStatesPut(chat_id, UserState.registrationVolunteerContact);
                    messageProvider.PutMessage(chat_id, "напишите ваши контакты");
                }
            }

        }
    }
}
