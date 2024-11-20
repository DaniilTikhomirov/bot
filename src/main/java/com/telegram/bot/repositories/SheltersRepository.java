package com.telegram.bot.repositories;

import com.telegram.bot.models.Shelters;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SheltersRepository extends JpaRepository<Shelters, Long> {
}
