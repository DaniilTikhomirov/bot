package com.telegram.bot.repositories;

import com.telegram.bot.models.Shelters;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SheltersRepository extends JpaRepository<Shelters, Long> {
    Optional<Shelters> getSheltersById(long id);
}
