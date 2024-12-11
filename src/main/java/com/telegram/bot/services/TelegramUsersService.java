package com.telegram.bot.services;


import com.telegram.bot.models.TelegramUser;
import com.telegram.bot.repositories.TelegramUsersRepository;
import org.springframework.stereotype.Service;

@Service
public class TelegramUsersService {
    private final TelegramUsersRepository telegramUsersRepository;

    public TelegramUsersService(TelegramUsersRepository telegramUsersRepository) {
        this.telegramUsersRepository = telegramUsersRepository;
    }

    public void addTelegramUsers(TelegramUser telegramUser) {
        telegramUsersRepository.save(telegramUser);
    }

    public TelegramUser getByTgIdTelegramUser(long tgId) {
        return telegramUsersRepository.findByTelegramId(tgId).orElse(null);
    }
}
