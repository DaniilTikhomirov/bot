package com.telegram.bot.services;

import com.telegram.bot.models.Animal;
import com.telegram.bot.repositories.AnimalRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

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
}
