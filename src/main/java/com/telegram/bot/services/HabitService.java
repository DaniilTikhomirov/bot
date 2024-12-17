package com.telegram.bot.services;

import com.telegram.bot.models.Habit;
import com.telegram.bot.repositories.HabitRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Сервис для управления привычками животных в системе приюта.
 *
 * <p>Содержит методы для добавления и получения информации о привычках.</p>
 * <p>
 * Связан с репозиторием {@link HabitRepository} для взаимодействия с базой данных.
 */

@Service
public class HabitService {

    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public Habit addHabit(Habit habit) {
        return habitRepository.save(habit);
    }

    public Collection<Habit> getAllHabits() {
        return habitRepository.findAll();
    }
}
