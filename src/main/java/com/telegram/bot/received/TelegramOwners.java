package com.telegram.bot.received;

import com.telegram.bot.markUps.MarkupsForAdmins;
import com.telegram.bot.markUps.MarkupsForInfo;
import com.telegram.bot.markUps.MarkupsForOwners;
import com.telegram.bot.models.*;
import com.telegram.bot.services.*;
import com.telegram.bot.states.OwnersStates;
import com.telegram.bot.telegram_utils.MessageProvider;
import com.telegram.bot.telegram_utils.ModelsHelper;
import com.telegram.bot.telegram_utils.StatesStorage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class TelegramOwners {

    private final OwnerSheltersService ownerSheltersService;
    private final StatesStorage statesStorage;
    private final MessageProvider messageProvider;
    private final SchedulesService schedulesService;
    private final SheltersService sheltersService;
    private final AnimalService animalService;
    private final VolunteersService volunteersService;
    private final PhotoManagerService photoManagerService;
    private final TelegramUsersService telegramUsersService;

    public TelegramOwners(OwnerSheltersService ownerSheltersService,
                          StatesStorage statesStorage,
                          MessageProvider messageProvider,
                          SchedulesService schedulesService,
                          MarkupsForOwners markupsForOwners,
                          SheltersService sheltersService,
                          AnimalService animalService, VolunteersService volunteersService, PhotoManagerService photoManagerService, TelegramUsersService telegramUsersService) {
        this.ownerSheltersService = ownerSheltersService;
        this.statesStorage = statesStorage;
        this.messageProvider = messageProvider;
        this.schedulesService = schedulesService;
        this.sheltersService = sheltersService;
        this.animalService = animalService;
        this.volunteersService = volunteersService;
        this.photoManagerService = photoManagerService;
        this.telegramUsersService = telegramUsersService;
    }


    public void receivedStateOwner(long chatId, String message) {
        if (!message.startsWith("/") && ownerSheltersService.isOwner(chatId)) {
            OwnersStates currentState = statesStorage.getOwnerStates().get(chatId);
            if (currentState == null) {
                return;
            }
            switch (currentState) {
                case REGISTERED_SHELTERS_NAME -> handleRegisterName(chatId, message);
                case REGISTERED_SHELTERS_DESCRIPTION -> handelRegisterDescription(chatId, message);
                case REGISTRATION_SHELTERS_SECURITY_CONTACT -> handelRegisterSecurityContact(chatId, message);
                case REGISTRATION_SHELTERS_SCHEDULE -> handelRegisterSchedule(chatId, message);
                case REGISTRATION_SHELTERS_SAFETY_RECOMMENDATION -> handelSafetyRecommendation(chatId, message);
                case REGISTRATION_SHELTERS_CONTACT -> handelRegisterContact(chatId, message);
                case REGISTRATION_SHELTERS_WAIT -> messageProvider.PutMessage(chatId, "Дождитесь ответа администратора");
                case REGISTRATION_ANIMAL_DESCRIPTION -> handleRegisterAnimalDescription(chatId, message);
                case REGISTRATION_ANIMAL_COLOR -> handleRegisterAnimalColor(chatId, message);
                case REJECT_VOLUNTEER -> {messageProvider.PutMessage(statesStorage.getVolunteerRejectedID().get(chatId),
                        "вам отказали по причине: " + message);}
                case GET_CHAT_ID_PUT_MESSAGE -> handlePutAnimal(chatId, message);
            }
        }
    }

    private void handlePutAnimal(long chatId, String message) {
        long userID = Long.parseLong(message);
        TelegramUser telegramUser = telegramUsersService.getByTgIdTelegramUser(userID);
        if(telegramUser == null) {
            messageProvider.PutMessage(chatId, "такого пользователя не существует попробуйте еще раз");
            return;
        }

        messageProvider.PutMessageWithMarkUp(userID, "Потвердите что вы хотите взять животное",
                MarkupsForAdmins.rejectAcceptMarkUp(chatId, 1, "rejectUs", "acceptUs"));
    }

    private void handleRegisterName(long chatId, String message) {
        OwnerShelters ownerShelters = ownerSheltersService.getOwnerShelterByTelegramId(chatId);

        Shelters shelter = new Shelters();
        shelter.setName(message);

        ownerShelters.getShelters().add(shelter);

        statesStorage.registrationDataOwnerPut(chatId, ownerShelters);
        statesStorage.getOwnerStates().put(chatId, OwnersStates.REGISTERED_SHELTERS_DESCRIPTION);
        messageProvider.PutMessage(chatId, "Введите описание приюта");
    }

    private void handelRegisterDescription(long chatId, String message) {
        OwnerShelters ownerShelters = statesStorage.getRegistrationDataOwner().get(chatId);
        List<Shelters> shelters = new ArrayList<>(ownerShelters.getShelters());

        Shelters shelter = shelters.get(shelters.size() - 1);
        shelter.setDescription(message);

        ownerShelters.setShelters(shelters);
        statesStorage.registrationDataOwnerPut(chatId, ownerShelters);
        statesStorage.getOwnerStates().put(chatId, OwnersStates.REGISTRATION_SHELTERS_SECURITY_CONTACT);
        messageProvider.PutMessage(chatId, "Введите контакты охраны");
    }

    private void handelRegisterSecurityContact(long chatId, String message) {
        OwnerShelters ownerShelters = statesStorage.getRegistrationDataOwner().get(chatId);
        List<Shelters> shelters = new ArrayList<>(ownerShelters.getShelters());

        Shelters shelter = shelters.get(shelters.size() - 1);
        shelter.setSecurityContact(message);

        ownerShelters.setShelters(shelters);
        statesStorage.registrationDataOwnerPut(chatId, ownerShelters);
        statesStorage.getOwnerStates().put(chatId, OwnersStates.REGISTRATION_SHELTERS_SCHEDULE);
        messageProvider.PutMessage(chatId, """
                введите расписание приюта в формате:
                Понедельник 11-11
                Вторник 11-11
                Среда 11-11
                Четверг 11-11
                Пятница 11-11
                Суббота 11-11
                Воскресенье 11-11""");
    }

    private void handelRegisterSchedule(long chatId, String message) {
        if (schedulesService.isValidSchedule(message)) {
            OwnerShelters ownerShelters = statesStorage.getRegistrationDataOwner().get(chatId);
            List<Shelters> shelters = new ArrayList<>(ownerShelters.getShelters());
            Schedules schedules = schedulesService.getSchedule(message);

            Shelters shelter = shelters.get(shelters.size() - 1);
            shelter.setSchedules(schedules);

            ownerShelters.setShelters(shelters);
            statesStorage.registrationDataOwnerPut(chatId, ownerShelters);
            statesStorage.getOwnerStates().put(chatId, OwnersStates.REGISTRATION_SHELTERS_KIND);
            messageProvider.PutMessageWithMarkUp(chatId, "укажите тип приюта cat или dog",
                    MarkupsForOwners.variantsForKind(chatId));
        } else {
            messageProvider.PutMessage(chatId, "не правильно введены данные попробуйте еще раз");
        }
    }

    private void handelSafetyRecommendation(long chatId, String message) {
        OwnerShelters ownerShelters = statesStorage.getRegistrationDataOwner().get(chatId);
        List<Shelters> shelters = new ArrayList<>(ownerShelters.getShelters());

        Shelters shelter = shelters.get(shelters.size() - 1);
        shelter.setSafetyRecommendation(message);

        ownerShelters.setShelters(shelters);
        statesStorage.registrationDataOwnerPut(chatId, ownerShelters);
        statesStorage.getOwnerStates().put(chatId, OwnersStates.REGISTRATION_SHELTERS_CONTACT);
        messageProvider.PutMessage(chatId, "Введите контакты для связи");
    }

    private void handelRegisterContact(long chatId, String message) {
        OwnerShelters ownerShelters = statesStorage.getRegistrationDataOwner().get(chatId);
        List<Shelters> shelters = new ArrayList<>(ownerShelters.getShelters());

        Shelters shelter = shelters.get(shelters.size() - 1);
        shelter.setContact(message);


        ownerShelters.setShelters(shelters);

        statesStorage.registrationDataOwnerPut(chatId, ownerShelters);

        String answerOwner = String.format(
                """
                        Владелец: %s
                        Имя приюта: %s
                        Описание приюта: %s
                        Телефон охраны: %s
                        Расписание:
                          Понедельник: %s
                          Вторник: %s
                          Среда: %s
                          Четверг: %s
                          Пятница: %s
                          Суббота: %s
                          Воскресенье: %s
                        Тип приюта: %s
                        Рекомендации по входу: %s
                        Контакты: %s""",
                ownerShelters.getName(),
                shelter.getName(),
                shelter.getDescription(),
                shelter.getSecurityContact(),
                shelter.getSchedules().getMonday(),
                shelter.getSchedules().getTuesday(),
                shelter.getSchedules().getWednesday(),
                shelter.getSchedules().getThursday(),
                shelter.getSchedules().getFriday(),
                shelter.getSchedules().getSaturday(),
                shelter.getSchedules().getSunday(),
                shelter.getKind(),
                shelter.getSafetyRecommendation(),
                shelter.getContact()
        );

        if (statesStorage.getOwnerStates().get(chatId) == OwnersStates.REGISTRATION_SHELTERS_CONTACT)
            messageProvider.SendRegistrationToAdministrators(chatId, answerOwner, "reject_shelters", "accept_shelters");

        statesStorage.getOwnerStates().put(chatId, OwnersStates.REGISTRATION_SHELTERS_WAIT);
        messageProvider.PutMessage(chatId, "приют отправлен на рассмотрение");
    }

    private void handleRegisterAnimalDescription(long chatId, String message) {
        Shelters shelters = statesStorage.getOwnerRegisterShelters().get(chatId);

        Animal animal = new Animal();

        animal.setKind(shelters.getKind());

        animal.setDescription(message);

        statesStorage.ownerRegisterAnimalsPut(chatId, animal);

        statesStorage.ownerStatesPut(chatId, OwnersStates.REGISTRATION_ANIMAL_COLOR);

        messageProvider.PutMessage(chatId, "напишите цвет животного");

    }

    private void handleRegisterAnimalColor(long chatId, String message) {
        Animal animal = statesStorage.getOwnerRegisterAnimals().get(chatId);
        Shelters shelters = statesStorage.getOwnerRegisterShelters().get(chatId);

        animal.setShelters(shelters);

        animal.setColor(message);
        List<Animal> animals = new ArrayList<>(shelters.getAnimals());

        animals.add(animal);

        shelters.setAnimals(animals);

        animal = animalService.addAnimal(animal);

        statesStorage.ownerRegisterAnimalsPut(chatId, animal);
        statesStorage.ownerRegisterSheltersPut(chatId, shelters);

        statesStorage.ownerStatesPut(chatId, OwnersStates.REGISTRATION_ANIMAL_PHOTO);

        messageProvider.PutMessageWithMarkUp(chatId, "отправьте фотографии",
                MarkupsForOwners.acceptPhoto());
    }

    public void ownerCallBack(long chat_id, String[] call_split_data, int messageId, int page, int size) {
        if (ownerSheltersService.isOwner(chat_id)) {
            switch (call_split_data[0]) {
                case "add_shelters" -> {
                    messageProvider.PutMessage(chat_id, "Введите имя приюта");
                    statesStorage.getOwnerStates().put(chat_id, OwnersStates.REGISTERED_SHELTERS_NAME);
                }
                case "cat", "dog" -> {
                    if (statesStorage.getOwnerStates().get(chat_id) == OwnersStates.REGISTRATION_SHELTERS_KIND) {
                        handelRegisterKind(chat_id, call_split_data[0]);
                    }
                }
                case "add_animal" -> {
                    List<Shelters> shelters = ownerSheltersService.getPageShelters(page, size, chat_id);
                    if (shelters.isEmpty()) {
                        messageProvider.PutMessage(chat_id, "для добавления животных надо добавить приюты");
                        return;
                    }
                    messageProvider.delMessage(chat_id, messageId);
                    messageProvider.PutMessageWithMarkUp(chat_id, "выберете приют",
                            MarkupsForOwners.allShelters(chat_id, messageId, page, shelters));
                }

                case "put_animal", "back_animal_put_animal" -> {
                    List<Shelters> shelters = ownerSheltersService.getPageShelters(page, size, chat_id);
                    messageProvider.changeText(chat_id, messageId, "выберете приют из которого заберают животное");
                    messageProvider.changeInline(chat_id, messageId,
                            MarkupsForOwners.allSheltersForPutAnimal(chat_id, messageId, page, shelters));

                }

                case "click_on_shelter_put_animal" -> {
                    List<Animal> animals = sheltersService.getPageAnimals(page, size,
                            Long.parseLong(call_split_data[2]), true);
                    messageProvider.changeText(chat_id,messageId, "Выберете животное");
                    messageProvider.changeInline(chat_id, messageId,
                            MarkupsForOwners.allAnimalsForPutAnimal(chat_id, messageId, 1, animals,
                                    Long.parseLong(call_split_data[2])));

                }

                case "back_volunteer_put_animal" -> {
                    List<Animal> animals = sheltersService.getPageAnimals(page, size,
                            Long.parseLong(call_split_data[3]), true);
                    messageProvider.changeText(chat_id,messageId, "Выберете животное");
                    messageProvider.changeInline(chat_id, messageId,
                            MarkupsForOwners.allAnimalsForPutAnimal(chat_id, messageId, 1, animals,
                                    Long.parseLong(call_split_data[3])));
                }

                case "click_on_volunteer_put_animal" -> {
                    statesStorage.ownerStatesPut(chat_id, OwnersStates.GET_CHAT_ID_PUT_MESSAGE);
                    statesStorage.volunteersForPutAnimalPut(chat_id,
                            volunteersService.getVolunteerById(Long.parseLong(call_split_data[2])));
                    messageProvider.PutMessage(chat_id, "напишите id пользователя, который забирает животное");
                }

                case "click_on_animal_put_animal" -> {
                    List<Volunteers> volunteers = sheltersService.
                            getShelterById(Long.parseLong(call_split_data[3])).getVolunteers().stream().toList();

                    List<Volunteers> volunteersPage = ModelsHelper.getPage(page, size, volunteers);
                    Animal animal = animalService.getAnimalById(Long.parseLong(call_split_data[2]));

                    statesStorage.animalForPutPut(chat_id, animal);

                    messageProvider.changeText(chat_id, messageId, "Выберете волонтера");

                    messageProvider.changeInline(chat_id, messageId,MarkupsForOwners.VolunteersForPutAnimal(chat_id, messageId, page, volunteersPage,
                            Long.parseLong(call_split_data[3])));
                }

                case "info_about_animal" -> {
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
                            Long.parseLong(call_split_data[3]), "back_from_animals_owner"));
                }

                case "back_from_animals_owner" -> {
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

                    messageProvider.changeText(chat_id, messageId, "Ваши животные:");
                    messageProvider.changeInline(chat_id, messageId,
                            MarkupsForInfo.getAnimals(chat_id, messageId, page, animals,
                                    Long.parseLong(call_split_data[2])));
                }

                case "click_on_shelter" -> {
                    messageProvider.delMessage(chat_id, messageId);
                    Shelters shelters = sheltersService.getShelterById(Long.parseLong(call_split_data[2]));

                    if (shelters != null){
                        String kind = shelters.getKind();

                        if (kind.equals("cat")){
                            kind = "кота";
                        }else if (kind.equals("dog")){
                            kind = "собаки";
                        }
                        messageProvider.PutMessage(chat_id, "введите описание " + kind);
                        statesStorage.ownerStatesPut(chat_id, OwnersStates.REGISTRATION_ANIMAL_DESCRIPTION);
                        statesStorage.ownerRegisterSheltersPut(chat_id, shelters);
                    }else {
                        messageProvider.PutMessage(chat_id, "произошла ошибка попробуйте еще раз!");
                    }

                }case "getInfoAboutShelters", "back_to_shelters" -> {
                    OwnerShelters ownerShelters = ownerSheltersService.getOwnerShelterByTelegramId(chat_id);
                    messageProvider.changeText(chat_id, messageId, "Ваши приюты:");
                    messageProvider.changeInline(chat_id, messageId, MarkupsForInfo.getInfoShelters(chat_id, messageId,
                            page, ownerShelters.getShelters().stream().toList()));
                }case "back_to_profile" -> {
                    messageProvider.delMessage(chat_id, messageId);
                    getInformation(chat_id);
                } case "back_shelter_put_animal" -> {

                    OwnerShelters ownerShelters = ownerSheltersService.getOwnerShelterByTelegramId(chat_id);
                    if (ownerShelters == null) {
                        messageProvider.PutMessage(chat_id, "вы не владелец приюта если хотите зарегистрировать" +
                                "свой приют ведите команду /register_owner");
                        return;
                    }
                    messageProvider.changeText(chat_id, messageId, "добро пожаловать " + ownerShelters.getName() +
                            " выберете нужную команду");

                    messageProvider.changeInline(chat_id, messageId, MarkupsForOwners.variantsForAdded(chat_id));
                }case "info_on_shelter" -> {
                    List<Animal> animals = sheltersService.getPageAnimals(page, size,
                            Long.parseLong(call_split_data[2]), true);
                    messageProvider.changeText(chat_id, messageId, "Ваши животные:");
                    messageProvider.changeInline(chat_id, messageId,
                            MarkupsForInfo.getAnimals(chat_id, messageId, page, animals,
                                    Long.parseLong(call_split_data[2])));
                }case "acceptV" -> {
                    statesStorage.getUserStates().remove(chat_id);
                    volunteersService.addVolunteers(statesStorage.getRegistrationDataVolunteers()
                            .get(Long.parseLong(call_split_data[1])));
                    messageProvider.PutMessage(Long.parseLong(call_split_data[1]), "вашу заявку приняли");
                    messageProvider.delVolunteerMessage(call_split_data);
                }case "rejectV" -> {
                    statesStorage.getUserStates().remove(chat_id);
                    messageProvider.PutMessage(chat_id,"напишите причину");
                    statesStorage.ownerStatesPut(chat_id, OwnersStates.REJECT_VOLUNTEER);
                    statesStorage.getVolunteerRejectedID().put(chat_id, Long.parseLong(call_split_data[1]));
                    messageProvider.delVolunteerMessage(call_split_data);
                }
            }
        }
    }

    private void handelRegisterKind(long chatId, String message) {
        OwnerShelters ownerShelters = statesStorage.getRegistrationDataOwner().get(chatId);
        List<Shelters> shelters = new ArrayList<>(ownerShelters.getShelters());

        Shelters shelter = shelters.get(shelters.size() - 1);
        shelter.setKind(message);

        ownerShelters.setShelters(shelters);
        statesStorage.registrationDataOwnerPut(chatId, ownerShelters);
        statesStorage.getOwnerStates().put(chatId, OwnersStates.REGISTRATION_SHELTERS_SAFETY_RECOMMENDATION);
        messageProvider.PutMessage(chatId, "Введите рекомендации по входу через охрану");
    }

    public void getInformation(long chat_id) {
        if (!ownerSheltersService.isOwner(chat_id)){
            messageProvider.PutMessage(chat_id, "Вы не владелец если хотите зарегистрироваться /owner");
            return;
        }
        OwnerShelters ownerShelters = ownerSheltersService.getOwnerShelterByTelegramId(chat_id);
        if (ownerShelters == null) {
            messageProvider.PutMessage(chat_id, "произошла ошибка попробуйте еще раз /start");
            return;
        }
        String info = String.format("владелец: <b>%s</b>",
                ownerShelters.getName());

        messageProvider.PutMessageWithMarkUp(chat_id, info, MarkupsForInfo.getInfoAboutShelters(chat_id));
    }
}
