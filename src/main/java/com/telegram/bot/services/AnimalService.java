package com.telegram.bot.services;

import com.telegram.bot.models.Animal;
import com.telegram.bot.repositories.AnimalRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Сервис для управления животными в системе приюта.
 *
 * <p>Содержит методы для добавления и получения информации о животных.</p>
 * <p>
 * Связан с репозиторием {@link AnimalRepository} для взаимодействия с базой данных
 */
@Service
public class AnimalService {

    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Animal addAnimal(Animal animal) {
        return animalRepository.save(animal);
    }

    public Collection<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    public Animal getAnimalById(long id) {
        return animalRepository.findById(id).orElse(null);
    }
}
