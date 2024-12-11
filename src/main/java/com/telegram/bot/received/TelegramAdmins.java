package com.telegram.bot.received;

import com.telegram.bot.services.OwnerSheltersService;
import com.telegram.bot.states.AdministratorStates;
import com.telegram.bot.states.UserStateRegisterOwner;
import com.telegram.bot.telegram_utils.MessageProvider;
import com.telegram.bot.telegram_utils.StatesStorage;
import org.springframework.stereotype.Component;

@Component
public class TelegramAdmins {

    private final MessageProvider messageProvider;

    private final StatesStorage statesStorage;

    private final OwnerSheltersService ownerSheltersService;

    public TelegramAdmins(MessageProvider messageProvider,
                          StatesStorage statesStorage,
                          OwnerSheltersService ownerSheltersService) {
        this.messageProvider = messageProvider;
        this.statesStorage = statesStorage;
        this.ownerSheltersService = ownerSheltersService;
    }

    /**
     * Обрабатывает состояния администратора в процессе взаимодействия с ботом.
     *
     * <p>Метод анализирует текущее состояние администратора и выполняет действия:</p>
     * <ul>
     *     <li>При отклонении заявки отправляет пользователю причину отказа.</li>
     * </ul>
     *
     * @param chatId  идентификатор чата администратора в Telegram.
     * @param message сообщение администратора с причиной отклонения.
     */
    public void receivedStateAdmin(long chatId, String message) {
        if (statesStorage.getAdministratorStates().get(chatId) == AdministratorStates.REJECTED_DESCRIPTION) {
            messageProvider.PutMessage(statesStorage.getAdministratorRejectedID().get(chatId), "ваша заявка отклонена \n описание: " + message);
            statesStorage.UserStatesPut(statesStorage.getAdministratorRejectedID().get(chatId),
                    UserStateRegisterOwner.registrationOwnerDenied);
            statesStorage.getAdministratorRejectedID().remove(chatId);
            statesStorage.getRegistrationDataOwner().remove(chatId);
        }
    }

    public void administratorCallBack(long chat_id, String[] call_split_data) {
        long id = Long.parseLong(call_split_data[1]);

        switch (call_split_data[0]) {
            case "accept" -> {
                statesStorage.UserStatesPut(id, UserStateRegisterOwner.registrationOwnerAccepted);
                messageProvider.PutMessage(id, "Вашу заявку приняли!");
                ownerSheltersService.addOwnerShelter(statesStorage.getRegistrationDataOwner().get(id));
                statesStorage.getRegistrationDataOwner().remove(id);
                messageProvider.delAdminMessage(call_split_data);
            }
            case "reject", "reject_shelters" -> {
                messageProvider.PutMessage(chat_id, "Напишите причину отмены заявки");
                statesStorage.getAdministratorStates().put(chat_id, AdministratorStates.REJECTED_DESCRIPTION);
                statesStorage.getAdministratorRejectedID().put(chat_id, id);
                messageProvider.delAdminMessage(call_split_data);
            }
            case "accept_shelters" -> {
                messageProvider.PutMessage(id, "ваш приют одобрили!");
                ownerSheltersService.addOwnerShelter(statesStorage.getRegistrationDataOwner().get(id));
                statesStorage.getRegistrationDataOwner().remove(id);
                statesStorage.getUserStates().remove(id);
                messageProvider.delAdminMessage(call_split_data);
            }
        }
    }
}
