package com.telegram.bot.controller.bot;

import com.telegram.bot.callBack.CallBack;
import com.telegram.bot.config.BotConfig;
import com.telegram.bot.markUps.MarkupsForInfo;
import com.telegram.bot.models.Shelters;
import com.telegram.bot.models.TelegramUser;
import com.telegram.bot.received.TelegramAdmins;
import com.telegram.bot.received.TelegramOwners;
import com.telegram.bot.received.TelegramUsers;
import com.telegram.bot.markUps.MarkupsForOwners;
import com.telegram.bot.models.OwnerShelters;
import com.telegram.bot.services.*;
import com.telegram.bot.states.AdministratorStates;
import com.telegram.bot.states.OwnersStates;
import com.telegram.bot.states.UserStateRegisterOwner;
import com.telegram.bot.telegram_utils.MessageProvider;
import com.telegram.bot.telegram_utils.StatesStorage;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Телеграм-бот для управления процессом регистрации владельцев приютов и взаимодействия с администраторами.
 *
 * <p>Этот класс реализует логику для обработки взаимодействий пользователей с ботом, включая:
 * регистрацию владельцев приютов, отправку заявок администраторам, обработку ответов администраторов
 * и отправку уведомлений.</p>
 *
 * <h3>Основные функции:</h3>
 * <ul>
 *     <li>Обработка входящих сообщений и команд (например, /start, /register_owner, /owner).</li>
 *     <li>Управление состоянием пользователей через {@link UserStateRegisterOwner}.</li>
 *     <li>Регистрация заявок владельцев приютов с отправкой сообщений администраторам.</li>
 *     <li>Обработка решений администраторов (принятие или отклонение заявок).</li>
 *     <li>Удаление временных сообщений из чата для удобства.</li>
 * </ul>
 *
 * <h3>Используемые компоненты:</h3>
 * <ul>
 *     <li>{@link BotConfig} — конфигурация для получения токена и имени бота.</li>
 *     <li>{@link OwnerSheltersService} — управление данными владельцев приютов.</li>
 *     <li>{@link AdministratorService} — управление данными администраторов.</li>
 *     <li>{@link UserStateRegisterOwner} — состояния пользователя при регистрации владельца приюта.</li>
 *     <li>{@link AdministratorStates} — состояния администратора при обработке заявок.</li>
 * </ul>
 *
 * <h3>Обработка обновлений:</h3>
 * <p>Все входящие обновления обрабатываются методом {@link #onUpdateReceived(Update)}.
 * Команды (например, /start, /register_owner) и callback-запросы (например, подтверждение/отклонение заявок)
 * обрабатываются отдельно.</p>
 *
 * <h3>Примеры использования:</h3>
 * <p>После отправки команды <code>/register_owner</code> пользователь начнет процесс регистрации как владелец приюта.</p>
 *
 * <h3>Обработка ошибок:</h3>
 * <p>Любые ошибки Telegram API обрабатываются через логирование и выброс исключения {@link RuntimeException}.</p>
 *
 * <p>Класс скрыт от документации Swagger с помощью аннотации {@link Hidden}.</p>
 *
 * @see TelegramLongPollingBot
 * @see UserStateRegisterOwner
 * @see AdministratorStates
 */
@Slf4j
@Hidden
@Component
public class TelegramPetBot extends TelegramLongPollingBot {

    private final BotConfig config;

    private final OwnerSheltersService ownerSheltersService;

    private final StatesStorage statesStorage;

    private final MarkupsForOwners markupsForOwners;

    private final TelegramUsers telegramUsers;

    private final MessageProvider messageProvider;

    private final TelegramAdmins telegramAdmins;

    private final TelegramOwners telegramOwners;

    private final CallBack callBack;

    private final PhotoManagerService photoManagerService;

    private final TelegramUsersService telegramUsersService;
    private final SheltersService sheltersService;

    public TelegramPetBot(BotConfig config,
                          OwnerSheltersService ownerSheltersService,
                          MarkupsForOwners markupsForOwners,
                          StatesStorage statesStorage,
                          TelegramUsers telegramUsers,
                          MessageProvider messageProvider,
                          TelegramAdmins telegramAdmins,
                          TelegramOwners telegramOwners,
                          CallBack callBack,
                          PhotoManagerService photoManagerService,
                          TelegramUsersService telegramUsersService, SheltersService sheltersService) {
        super(config.getBotToken());
        this.config = config;
        this.statesStorage = statesStorage;
        this.ownerSheltersService = ownerSheltersService;
        this.markupsForOwners = markupsForOwners;
        this.telegramUsers = telegramUsers;
        this.messageProvider = messageProvider;
        this.telegramAdmins = telegramAdmins;
        this.telegramOwners = telegramOwners;
        this.callBack = callBack;
        this.photoManagerService = photoManagerService;
        this.telegramUsersService = telegramUsersService;
        this.sheltersService = sheltersService;
    }

    /**
     * Обрабатывает входящие обновления от пользователей Telegram.
     *
     * <p>Проверяет тип обновления (сообщение или колбэк) и выполняет соответствующие действия:</p>
     * <ul>
     *     <li>Регистрация владельцев приютов через команды;</li>
     *     <li>Управление состояниями пользователей и администраторов;</li>
     *     <li>Обработка колбэк-команд администраторов для заявок.</li>
     * </ul>
     *
     * @param update объект {@link Update}, представляющий обновление от Telegram.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            int messageId = update.getMessage().getMessageId();

            System.out.println(statesStorage.getUserStates().toString());

            telegramUsers.receivedStateUser(chatId, message);

            telegramAdmins.receivedStateAdmin(chatId, message);

            telegramOwners.receivedStateOwner(chatId, message);


            switch (message) {
                case "/start" -> {
                    StartMessage(chatId, update.getMessage().getChat().getFirstName());
                    if (telegramUsersService.getByTgIdTelegramUser(chatId) == null) {
                        TelegramUser telegramUser = new TelegramUser();
                        telegramUser.setTelegramId(chatId);
                        telegramUsersService.addTelegramUsers(telegramUser);
                    }
                    statesStorage.removeChatId(chatId);
                }

                case "/owner" -> {
                    OwnerMessage(chatId);
                }

                case "/owner_info" -> {
                    telegramOwners.getInformation(chatId);
                }

                case "/register_owner" -> {
                    RegistrationOwnerMessage(chatId);
                }

                case "/info" -> {
                    messageProvider.PutMessage(chatId, "дополнить");
                }

                case "/help" -> {
                    messageProvider.PutMessage(chatId, "дополнить");
                }

                case "/shelters" -> {
                    List<Shelters> shelters = sheltersService.getPageAllShelters(1, 5);

                    messageProvider.PutMessageWithMarkUp(chatId, "Все приюты:",
                            MarkupsForInfo.getInfoAllShelters(chatId, messageId,
                                    1, shelters, false));
                }

                case "сохранить фотографии" -> {
                    if (statesStorage.getOwnerStates().get(chatId) == OwnersStates.REGISTRATION_ANIMAL_PHOTO){
                        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();

                        replyKeyboardRemove.setRemoveKeyboard(true);
                        replyKeyboardRemove.setSelective(true);
                        messageProvider.PutMessageWithMarkUp(chatId,"фотографии и питомец сохранены!",
                                replyKeyboardRemove);
                        statesStorage.removeChatId(chatId);
                    }

                }

            }

        } else if (update.hasCallbackQuery()) {
            callBack.callBackReaction(update);
        } else if (update.hasMessage() && update.getMessage().hasPhoto()) {
            long chatId = update.getMessage().getChatId();
            if (statesStorage.getOwnerStates().get(chatId) == OwnersStates.REGISTRATION_ANIMAL_PHOTO) {
                photoManagerService.downloadFile(update);
            }
        }
    }


    /**
     * Возвращает имя бота, указанное в конфигурации.
     *
     * @return имя бота из {@link BotConfig}.
     */
    @Override
    public String getBotUsername() {
        return this.config.getBotName();
    }

    /**
     * Начинает процесс регистрации владельца приюта.
     *
     * <p>Если пользователь уже зарегистрирован, отправляет сообщение с соответствующей информацией.</p>
     *
     * @param chatId идентификатор чата пользователя.
     */
    private void RegistrationOwnerMessage(long chatId) {
        if (ownerSheltersService.isOwner(chatId)) {
            messageProvider.PutMessage(chatId,
                    "вы уже владелец приюта информацию можете узнать по команде /owner");
        } else {
            statesStorage.UserStatesPut(chatId, UserStateRegisterOwner.registrationOwner);
            messageProvider.PutMessage(chatId, "напишите пожалуйста ваше имя не указывайте в имени '/'");
        }
    }


    private void OwnerMessage(long chatId) {
        OwnerShelters ownerShelters = ownerSheltersService.getOwnerShelterByTelegramId(chatId);
        if (ownerShelters == null) {
            messageProvider.PutMessage(chatId, "вы не владелец приюта если хотите зарегистрировать" +
                    "свой приют ведите команду /register_owner");
        } else {
            messageProvider.PutMessageWithMarkUp(chatId, "добро пожаловать " + ownerShelters.getName() +
                    " выберете нужную команду", markupsForOwners.variantsForAdded(chatId));
        }
    }

    /**
     * Отправляет приветственное сообщение пользователю.
     *
     * @param chatId идентификатор чата Telegram.
     * @param name   имя пользователя, для персонализации приветствия.
     */
    private void StartMessage(long chatId, String name) {
        String answer = "Привет " + name;
        messageProvider.PutMessage(chatId, answer);
    }

    public void setConfig(){

        List<BotCommand> listOfCommands = new ArrayList<>();

        listOfCommands.add(new BotCommand("/start", "start or restart this bot | запуск или перезапуск бота"));
        listOfCommands.add(new BotCommand("/owner", "become an owner | стать владельцем"));
        listOfCommands.add(new BotCommand("/owner_info", "get detailed information about the owner | получить детальную информацию о владельце"));
        listOfCommands.add(new BotCommand("/register_owner", "register a new owner | зарегистрировать нового владельца"));
        listOfCommands.add(new BotCommand("/info", "general information about the bot | общая информация о боте"));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
