package com.telegram.bot.repositories;

import com.telegram.bot.models.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TelegramUsersRepository extends JpaRepository<TelegramUser, Long> {
    Optional<TelegramUser> findByTelegramId(Long id);
}
