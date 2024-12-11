package com.telegram.bot.telegram_utils;

import com.telegram.bot.models.Animal;
import com.telegram.bot.models.OwnerShelters;
import com.telegram.bot.models.Shelters;
import com.telegram.bot.states.AdministratorStates;
import com.telegram.bot.states.OwnersStates;
import com.telegram.bot.states.UserStateRegisterOwner;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
public class StatesStorage {

    private final Map<Long, UserStateRegisterOwner> userStates;

    private final Map<Long, OwnerShelters> registrationDataOwner;

    private final Map<Long, AdministratorStates> administratorStates;

    private final Map<Integer, Map<Long, Integer>> delMessages;

    private final Map<Long, OwnersStates> ownerStates;

    private final Map<Long, Long> administratorRejectedID;

    private final Map<Long, Animal> ownerRegisterAnimals;

    private final Map<Long, Shelters> ownerRegisterShelters;

    private final Map<Long, List<Integer>> photosForDelete;

    private int counterForDelMessage = 0;

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
    }

    public void incrementDelMessage(){
        counterForDelMessage++;
    }

    public void removeChatId(long chatId) {
        userStates.remove(chatId);
        registrationDataOwner.remove(chatId);
        administratorStates.remove(chatId);
        ownerStates.remove(chatId);
    }

    public void UserStatesPut(Long chatId, UserStateRegisterOwner userStateRegisterOwner) {
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

    public void ownerRegisterAnimalsPut(Long chatId, Animal animal) {
        ownerRegisterAnimals.put(chatId, animal);
    }

    public void ownerRegisterSheltersPut(Long chatId, Shelters shelters) {
        ownerRegisterShelters.put(chatId, shelters);
    }
}
