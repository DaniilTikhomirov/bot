package com.telegram.bot.services;

import com.telegram.bot.config.BotConfig;
import com.telegram.bot.telegram_utils.MessageProvider;
import com.telegram.bot.telegram_utils.StatesStorage;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PhotoManagerService extends TelegramLongPollingBot {

    private final StatesStorage statesStorage;

    private final BotConfig config;
    private final MessageProvider messageProvider;

    public PhotoManagerService(StatesStorage statesStorage, BotConfig config, MessageProvider messageProvider) {
        super(config.getBotToken());
        this.config = config;
        this.statesStorage = statesStorage;
        this.messageProvider = messageProvider;
    }

    public void createFile(String path) {
        Path structure = Paths.get(path);

        try {
            if (Files.notExists(structure)) {
                Files.createDirectories(structure);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void downloadFile(Update update) {
        long chatId = update.getMessage().getChatId();
        List<PhotoSize> photos = update.getMessage().getPhoto();

        System.out.println(photos.toString());
        if (!photos.isEmpty()) {

            PhotoSize photo = photos.stream()
                    .max(Comparator.comparing(PhotoSize::getFileSize))
                    .orElse(photos.get(0));

            String fileId = photo.getFileId(); // Максимальный размер фото

            String filePath = "./data/shelter" +
                    statesStorage.getOwnerRegisterShelters().get(chatId).getId() + "/animal" +
                    statesStorage.getOwnerRegisterAnimals().get(chatId).getId();
            try {
                createFile(filePath);


                GetFile getFile = new GetFile(fileId);
                org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
                // Скачиваем файл
                downloadFile(file.getFilePath(), new File(filePath,
                        photo.getFileUniqueId() + ".jpg"));

            } catch (TelegramApiException e) {
                e.printStackTrace();
                System.err.println("Error during file download: " + e.getMessage());
            }
        }
    }

    private List<Path> getPhotosFromStructure(String shelterId, String animalId, String startFile) throws IOException {
        Path structure = Paths.get("./" + startFile + "/" + "shelter" + shelterId + "/animal" + animalId);
        if (Files.notExists(structure)) {
            return null;
        }

       return Files.walk(structure).filter(Files::isRegularFile).toList();

    }

    public void sendPhoto(long chat_id, String shelterId, String animalId, String startFile) throws IOException {

        // Получаем список путей к фотографиям
        List<Path> photos = getPhotosFromStructure(shelterId, animalId, startFile);

        if (photos == null || photos.isEmpty()) {
            messageProvider.PutMessage(chat_id, "Фотографии отсутствуют");
            return;
        }

        // Создаем список для InputMedia объектов
        List<InputMedia> mediaGroup = new ArrayList<>();

        for (Path path : photos) {
            File photoFile = path.toFile();

            if (!photoFile.exists() || !photoFile.isFile()) {
                System.err.println("Файл не найден: " + path.toString());
                continue; // Пропускаем отсутствующие файлы
            }

            // Создаем объект InputMediaPhoto для каждой фотографии
            InputMediaPhoto photo = new InputMediaPhoto();
            photo.setMedia(photoFile, photoFile.getName());
            mediaGroup.add(photo);

            // Telegram разрешает максимум 10 фотографий в одной группе
            if (mediaGroup.size() == 10) {
                sendMediaGroup(chat_id, mediaGroup);
                mediaGroup.clear(); // Очищаем список для следующей группы
            }
        }

        // Отправляем оставшиеся фотографии, если есть
        if (!mediaGroup.isEmpty()) {
            sendMediaGroup(chat_id, mediaGroup);
        }
    }

    private void sendMediaGroup(long chat_id, List<InputMedia> mediaGroup) {
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setChatId(chat_id);
        sendMediaGroup.setMedias(mediaGroup);

        try {
            List<Message> messages = execute(sendMediaGroup);
            for (Message message : messages) {
                statesStorage.photosForDeleteAdd(chat_id, message.getMessageId());
            }
            System.out.println("Группа фотографий отправлена успешно!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке группы фотографий: " + e.getMessage());
        }
    }



    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
}
