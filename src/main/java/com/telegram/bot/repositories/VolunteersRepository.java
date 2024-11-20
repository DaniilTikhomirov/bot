package com.telegram.bot.repositories;

import com.telegram.bot.models.Volunteers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteersRepository extends JpaRepository<Volunteers, Long> {
}
