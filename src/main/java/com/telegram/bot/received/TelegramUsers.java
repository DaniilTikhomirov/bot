package com.telegram.bot.received;

import com.telegram.bot.markUps.MarkupsForInfo;
import com.telegram.bot.models.*;
import com.telegram.bot.services.AnimalService;
import com.telegram.bot.services.PhotoManagerService;
import com.telegram.bot.services.SheltersService;
import com.telegram.bot.services.TelegramUsersService;
import com.telegram.bot.states.UserState;
import com.telegram.bot.telegram_utils.MessageProvider;
import com.telegram.bot.telegram_utils.StatesStorage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


@Getter
@Slf4j
@Component
public class TelegramUsers {

    private final StatesStorage statesStorage;
    private final MessageProvider messageProvider;
    private final SheltersService sheltersService;
    private final PhotoManagerService photoManagerService;
    private final AnimalService animalService;
    private final TelegramUsersService telegramUsersService;

    public TelegramUsers(StatesStorage statesStorage, MessageProvider messageProvider, SheltersService sheltersService, PhotoManagerService photoManagerService, AnimalService animalService, TelegramUsersService telegramUsersService) {
        this.statesStorage = statesStorage;
        this.messageProvider = messageProvider;
        this.sheltersService = sheltersService;
        this.photoManagerService = photoManagerService;
        this.animalService = animalService;
        this.telegramUsersService = telegramUsersService;
    }

    /**
     * Обрабатывает состояния пользователя в процессе взаимодействия с ботом.
     *
     * <p>Метод анализирует текущее состояние пользователя и выполняет соответствующие действия:</p>
     * <ul>
     *     <li>При регистрации владельца приюта запрашивает имя и описание владельца;</li>
     *     <li>Отправляет заявку на регистрацию администраторам;</li>
     *     <li>Сбрасывает состояние после завершения регистрации или отказа.</li>
     * </ul>
     *
     * @param chatId  идентификатор чата пользователя в Telegram.
     * @param message сообщение пользователя.
     */
    public void receivedStateUser(long chatId, String message) {
        if (!message.startsWith("/")) {


            if (statesStorage.getUserStates().get(chatId) == UserState.registrationOwner) {

                OwnerShelters ownerShelters = new OwnerShelters();
                ownerShelters.setName(message);
                ownerShelters.setTelegramId(chatId);
                statesStorage.registrationDataOwnerPut(chatId, ownerShelters);
                messageProvider.PutMessage(chatId, "расскажите о себе о вашем приюте прикрепите " +
                        "ссылки если они есть чтобы администратор вас одобрил");
                statesStorage.UserStatesPut(chatId, UserState.registrationOwnerName);

            } else if (statesStorage.getUserStates().get(chatId) == UserState.registrationOwnerName) {
                String send = "имя: " + statesStorage.getRegistrationDataOwner().get(chatId).getName() +
                        "\n" + "описание: " + message;
                messageProvider.SendRegistrationToAdministrators(chatId, send, "reject", "accept");
                messageProvider.PutMessage(chatId, "сообщение было отправлено администраторам ожидайте ответа");
                statesStorage.UserStatesPut(chatId, UserState.registrationOwnerWait);

            } else if (statesStorage.getUserStates().get(chatId) == UserState.registrationOwnerAccepted ||
                    statesStorage.getUserStates().get(chatId) == UserState.registrationOwnerDenied) {
                statesStorage.getUserStates().remove(chatId);

            } else if (statesStorage.getUserStates().get(chatId) == UserState.registrationOwnerWait) {
                messageProvider.PutMessage(chatId, "Пожалуйста ожидайте ответа администратора");
            }
        }
    }

    public void userCallBack(long chat_id, String[] call_split_data, int messageId, int page, int size) {
        switch (call_split_data[0]) {
            case "info_on_shelter_user" -> {
                List<Animal> animals = sheltersService.getPageAnimals(page, size,
                        Long.parseLong(call_split_data[2]), true);
                messageProvider.delMessage(chat_id, messageId);
                messageProvider.PutMessageWithMarkUp(chat_id, "Животные приюта",
                        MarkupsForInfo.getAnimalsUser(chat_id, messageId, page,
                                animals, Long.parseLong(call_split_data[2])));

            }case "back_from_animals" -> {
                List<Animal> animals = sheltersService.getPageAnimals(page, size,
                        Long.parseLong(call_split_data[2]), true);
                for(int mesId : statesStorage.getPhotosForDelete().get(chat_id)) {
                    try {
                        messageProvider.delMessage(chat_id, mesId);
                    }catch (Exception e) {
                        System.out.println(mesId);
                    }
                }
                statesStorage.getPhotosForDelete().remove(chat_id);

                messageProvider.delMessage(chat_id, messageId);
                messageProvider.PutMessageWithMarkUp(chat_id, "Животные приюта",
                        MarkupsForInfo.getAnimalsUser(chat_id, messageId, page,
                                animals, Long.parseLong(call_split_data[2])));

            }
            case "back_to_shelters_user" -> {
                List<Shelters> shelters = sheltersService.getPageAllShelters(1, 5);

                messageProvider.changeText(chat_id, messageId, "Все приюты:");
                messageProvider.changeInline(chat_id, messageId, MarkupsForInfo.getInfoAllShelters(chat_id, messageId,
                        1, shelters, false));
            }
            case "info_about_animal_user" -> {
                Animal animal = animalService.getAnimalById(Long.parseLong(call_split_data[2]));
                if (animal == null) {
                    messageProvider.PutMessage(chat_id,"возникла ошибка /start");
                    return;
                }

                try {
                    photoManagerService.sendPhoto(chat_id, call_split_data[3],
                            call_split_data[2], "data");
                }catch (IOException e){
                    e.printStackTrace();
                }

                messageProvider.delMessage(chat_id, messageId);

                messageProvider.PutMessageWithMarkUp(chat_id, "Информация:\nцвет: " + animal.getColor() +
                        "\n" + "описание: " + animal.getDescription(), MarkupsForInfo.photoBack(chat_id,
                        Long.parseLong(call_split_data[3]), "back_from_animals"));

            }

            case "acceptUs" -> {
                TelegramUser telegramUser = telegramUsersService.getByTgIdTelegramUser(chat_id);
                Volunteers volunteers = statesStorage.getVolunteersForPutAnimal()
                        .get(Long.parseLong(call_split_data[1]));

                Animal animal = statesStorage.getAnimalsForPut().get(Long.parseLong(call_split_data[1]));

                animal.setReview(true);

                System.out.println(volunteers.getTelegramId());

                telegramUser.setVolunteer(volunteers);

                telegramUsersService.addTelegramUsers(telegramUser);

                animalService.addAnimal(animal);

                messageProvider.delMessage(chat_id, messageId);

                messageProvider.PutMessage(Long.parseLong(call_split_data[1]), "Питомец назначен!");
            }
            case "rejectUs" -> {
                messageProvider.delMessage(chat_id, messageId);

                messageProvider.PutMessage(Long.parseLong(call_split_data[1]), "Пользователь отклонил запрос");
            }
        }
    }
}
