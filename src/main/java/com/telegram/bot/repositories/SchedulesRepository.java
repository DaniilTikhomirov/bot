package com.telegram.bot.repositories;

import com.telegram.bot.models.Schedules;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulesRepository extends JpaRepository<Schedules, Long> {
}
