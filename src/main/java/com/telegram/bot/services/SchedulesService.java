package com.telegram.bot.services;

import com.telegram.bot.models.Schedules;
import com.telegram.bot.repositories.SchedulesRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    public boolean isValidSchedule(String schedule) {
        String[] split = schedule.split("[\\s\\n]");
        System.out.println(Arrays.toString(split));
        System.out.println(split.length);
        return split.length == 14;
    }

    public Schedules getSchedule(String schedule) {
        String[] split = schedule.split("[\\s\\n]");
        Schedules schedules = new Schedules();
        schedules.setMonday(split[1].replace("\n", ""));
        schedules.setTuesday(split[3].replace("\n", ""));
        schedules.setWednesday(split[5].replace("\n", ""));
        schedules.setThursday(split[7].replace("\n", ""));
        schedules.setFriday(split[9].replace("\n", ""));
        schedules.setSaturday(split[11].replace("\n", ""));
        schedules.setSunday(split[13].replace("\n", ""));

        return schedules;
    }
}
