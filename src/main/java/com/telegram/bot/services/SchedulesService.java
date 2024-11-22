package com.telegram.bot.services;

import com.telegram.bot.models.Schedules;
import com.telegram.bot.repositories.SchedulesRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Сервис для управления расписаниями приютов в системе.
 *
 * <p>Содержит методы для добавления и получения информации о расписаниях.</p>
 * <p>
 * Связан с репозиторием {@link SchedulesRepository} для взаимодействия с базой данных.
 */
@Service
public class SchedulesService {

    private final SchedulesRepository schedulesRepository;

    public SchedulesService(SchedulesRepository schedulesRepository) {
        this.schedulesRepository = schedulesRepository;
    }

    public Schedules addSchedule(Schedules schedules) {
        return schedulesRepository.save(schedules);
    }

    public Collection<Schedules> getAllSchedules() {
        return schedulesRepository.findAll();
    }
}
