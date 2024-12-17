package com.telegram.bot.services;

import com.telegram.bot.models.Volunteers;
import com.telegram.bot.repositories.VolunteersRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Сервис для управления волонтерами в системе приюта.
 *
 * <p>Содержит методы для добавления и получения информации о волонтерах.</p>
 * <p>
 * Связан с репозиторием {@link VolunteersRepository} для взаимодействия с базой данных.
 */
@Service
public class VolunteersService {

    private final VolunteersRepository volunteersRepository;

    public VolunteersService(VolunteersRepository volunteersRepository) {
        this.volunteersRepository = volunteersRepository;
    }

    public Volunteers addVolunteers(Volunteers volunteers) {
        return volunteersRepository.save(volunteers);
    }

    public Collection<Volunteers> getAllVolunteers() {
        return volunteersRepository.findAll();
    }

    public Volunteers getVolunteerByTelegramId(long telegramId) {
        return volunteersRepository.findVolunteersByTelegramId(telegramId).orElse(null);
    }

    public boolean isVolunteer(long telegramId) {
        return getVolunteerByTelegramId(telegramId) != null;
    }

    public Volunteers getVolunteerById(long id) {
        return volunteersRepository.findById(id).orElse(null);
    }
}
