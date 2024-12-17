package com.telegram.bot.services;

import com.telegram.bot.models.Animal;
import com.telegram.bot.models.Shelters;
import com.telegram.bot.repositories.SheltersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Сервис для управления приютами в системе.
 *
 * <p>Содержит методы для добавления и получения информации о приютах.</p>
 * <p>
 * Связан с репозиторием {@link SheltersRepository} для взаимодействия с базой данных.
 */
@Service
@Slf4j
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

    public Shelters getShelterById(long id) {
        return sheltersRepository.getSheltersById(id).orElse(null);
    }

    public List<Animal> getPageAnimals(int page, int size, long id, boolean notReview) {

        if (page < 1) {
            log.error("Page number is less than 1");
            throw new IllegalArgumentException("page must be greater than 0");
        }

        Shelters shelter = getShelterById(id);

        if (shelter == null) return null;

        List<Animal> animals;

        if (notReview) {
            animals = shelter.getOnlyNotReviewedAnimals();
        }else {
            animals = shelter.getAnimals().stream().toList();
        }


        int i = (page * size) - size;

        if (i > animals.size() - 1) {
            return new ArrayList<>();
        }

        int maxPageSize = page * size;

        List<Animal> newAnimals = new ArrayList<>();

        while (i < maxPageSize && i < animals.size()) {
            newAnimals.add(animals.get(i));
            i++;
        }

        return newAnimals;
    }

    public List<Shelters> getPageAllShelters(int page, int size) {

        if (page < 1) {
            log.error("Page number is less than 1");
            throw new IllegalArgumentException("page must be greater than 0");
        }

        List<Shelters> shelters = getShelters().stream().toList();

        int i = (page * size) - size;

        if (i > shelters.size() - 1) {
            return new ArrayList<>();
        }

        int maxPageSize = page * size;

        List<Shelters> newShelters = new ArrayList<>();

        while (i < maxPageSize && i < shelters.size()) {
            newShelters.add(shelters.get(i));
            i++;
        }

        return newShelters;

    }

    public List<Shelters> getSheltersByName(String name) {
        return sheltersRepository.getSheltersByName(name);
    }
}
