package com.telegram.bot.telegram_utils;

import com.telegram.bot.models.Animal;
import com.telegram.bot.models.OwnerShelters;
import com.telegram.bot.models.Shelters;
import com.telegram.bot.models.Volunteers;
import com.telegram.bot.states.AdministratorStates;
import com.telegram.bot.states.OwnersStates;
import com.telegram.bot.states.UserState;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
public class StatesStorage {

    private final Map<Long, UserState> userStates;

    private final Map<Long, OwnerShelters> registrationDataOwner;

    private final Map<Long, AdministratorStates> administratorStates;

    private final Map<Integer, Map<Long, Integer>> delMessages;

    private final Map<Integer, Map<Long, Integer>> delMessagesVolunteers;

    private final Map<Long, OwnersStates> ownerStates;

    private final Map<Long, Long> administratorRejectedID;

    private final Map<Long, Long> volunteerRejectedID;

    private final Map<Long, Animal> ownerRegisterAnimals;

    private final Map<Long, Shelters> ownerRegisterShelters;

    private final Map<Long, List<Integer>> photosForDelete;

    private final Map<Long, Volunteers> registrationDataVolunteers;

    private final Map<Long, Integer> searchMessageId;

    private final Map<Long, List<Shelters>> sheltersForSearch;

    private final Map<Long, Volunteers> volunteersForPutAnimal;

    private final Map<Long, Animal> animalsForPut;

    private int counterForDelMessage = 0;

    private int counterForDelMessagesVolunteer = 0;

    public StatesStorage() {
        userStates = new ConcurrentHashMap<>();
        registrationDataOwner = new ConcurrentHashMap<>();
        administratorStates = new ConcurrentHashMap<>();
        ownerStates = new ConcurrentHashMap<>();
        administratorRejectedID = new ConcurrentHashMap<>();
        delMessages = new ConcurrentHashMap<>();
        ownerRegisterAnimals = new ConcurrentHashMap<>();
        ownerRegisterShelters = new ConcurrentHashMap<>();
        photosForDelete = new ConcurrentHashMap<>();
        registrationDataVolunteers = new ConcurrentHashMap<>();
        searchMessageId = new ConcurrentHashMap<>();
        sheltersForSearch = new ConcurrentHashMap<>();
        delMessagesVolunteers = new ConcurrentHashMap<>();
        volunteerRejectedID = new ConcurrentHashMap<>();
        volunteersForPutAnimal = new ConcurrentHashMap<>();
        animalsForPut = new ConcurrentHashMap<>();
    }

    public void incrementDelMessage(){
        counterForDelMessage++;
    }

    public void incrementDelMessagesVolunteer(){
        counterForDelMessagesVolunteer++;
    }


    public void removeChatId(long chatId) {
        if(userStates.get(chatId) != UserState.WAIT_ANIMAL_ACCEPTED) {
            userStates.remove(chatId);
        }
        registrationDataOwner.remove(chatId);
        administratorStates.remove(chatId);
        ownerStates.remove(chatId);
        registrationDataVolunteers.remove(chatId);
        volunteersForPutAnimal.remove(chatId);
    }

    public void UserStatesPut(Long chatId, UserState userStateRegisterOwner) {
        userStates.put(chatId, userStateRegisterOwner);
        System.out.println(userStates.toString());
    }

    public void registrationDataOwnerPut(Long chatId, OwnerShelters ownerShelters) {
        registrationDataOwner.put(chatId, ownerShelters);
    }

    public void administratorStatesPut(Long chatId, AdministratorStates administratorStates) {
        this.administratorStates.put(chatId, administratorStates);
    }

    public void ownerStatesPut(Long chatId, OwnersStates ownerStates) {
        this.ownerStates.put(chatId, ownerStates);
    }

    public void administratorRejectedIDPut(Long chatId, Long rejectedID) {
        administratorRejectedID.put(chatId, rejectedID);
    }

    public void photosForDeleteAdd(Long chatId, Integer photo) {
        List<Integer> photosId = photosForDelete.get(chatId);
        if (photosId == null) {
            photosId = new ArrayList<>();
        }
        photosId.add(photo);
        photosForDelete.put(chatId, photosId);
    }

    public void delMessagesPut(Integer messageId, Map<Long, Integer> delMessages) {
        this.delMessages.put(messageId, delMessages);
    }

    public void delMessagesVolunteersPut(Integer messageId, Map<Long, Integer> delMessagesVolunteers) {
        this.delMessagesVolunteers.put(messageId, delMessagesVolunteers);
    }

    public void ownerRegisterAnimalsPut(Long chatId, Animal animal) {
        ownerRegisterAnimals.put(chatId, animal);
    }

    public void ownerRegisterSheltersPut(Long chatId, Shelters shelters) {
        ownerRegisterShelters.put(chatId, shelters);
    }

    public void registrationDataVolunteersPut(long chatId, Volunteers volunteers) {
        registrationDataVolunteers.put(chatId, volunteers);
    }

    public void searchMessageIdPut(Long chatId, Integer messageId) {
        searchMessageId.put(chatId, messageId);
    }

    public void sheltersForSearchPut(Long chatId, List<Shelters> shelters){
        sheltersForSearch.put(chatId, shelters);
    }

    public void volunteersForPutAnimalPut(Long chatId, Volunteers volunteers) {
        volunteersForPutAnimal.put(chatId, volunteers);
    }

    public void animalForPutPut(Long chatId, Animal animal){
        animalsForPut.put(chatId, animal);
    }

}
