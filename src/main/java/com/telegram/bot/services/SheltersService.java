package com.telegram.bot.services;

import com.telegram.bot.models.Shelters;
import com.telegram.bot.repositories.SheltersRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Сервис для управления приютами в системе.
 *
 * <p>Содержит методы для добавления и получения информации о приютах.</p>
 * <p>
 * Связан с репозиторием {@link SheltersRepository} для взаимодействия с базой данных.
 */
@Service
public class SheltersService {

    private final SheltersRepository sheltersRepository;

    public SheltersService(SheltersRepository sheltersRepository) {
        this.sheltersRepository = sheltersRepository;
    }

    public Shelters addShelter(Shelters shelters) {
        return sheltersRepository.save(shelters);
    }

    public Collection<Shelters> getShelters() {
        return sheltersRepository.findAll();
    }
}
