package com.telegram.bot.repositories;

import com.telegram.bot.models.OwnerShelters;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OwnerSheltersRepository extends JpaRepository<OwnerShelters, Long> {
    @Transactional
    @Query("SELECT distinct o FROM owners_shelters o left JOIN FETCH o.shelters WHERE o.telegramId = :chatId")
    OwnerShelters findByTelegramId(@Param("chatId") long chatId);


}
