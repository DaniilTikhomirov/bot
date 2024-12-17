package com.telegram.bot.repositories;

import com.telegram.bot.models.Volunteers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VolunteersRepository extends JpaRepository<Volunteers, Long> {
    Optional<Volunteers> findVolunteersByTelegramId(long telegramId);
}
