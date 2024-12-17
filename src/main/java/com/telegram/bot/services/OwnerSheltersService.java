package com.telegram.bot.services;

import com.telegram.bot.models.OwnerShelters;
import com.telegram.bot.models.Shelters;
import com.telegram.bot.repositories.OwnerSheltersRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class OwnerSheltersService {

    private final OwnerSheltersRepository ownerSheltersRepository;

    public OwnerSheltersService(OwnerSheltersRepository ownerSheltersRepository) {
        this.ownerSheltersRepository = ownerSheltersRepository;
    }

    public OwnerShelters addOwnerShelter(OwnerShelters ownerShelters) {
        System.out.println(ownerShelters.toString());
        return ownerSheltersRepository.save(ownerShelters);
    }

    public Collection<OwnerShelters> getAllOwnerShelters() {
        return ownerSheltersRepository.findAll();
    }

    public OwnerShelters getOwnerShelterByTelegramId(long TelegramId) {
        return ownerSheltersRepository.findByTelegramId(TelegramId);
    }

    @Transactional
    public boolean isOwner(long id) {
        OwnerShelters ownerShelters = getOwnerShelterByTelegramId(id);
        return ownerShelters != null;
    }

    public int getSizeShelters(long telegramId) {
        OwnerShelters ownerShelters = ownerSheltersRepository.findByTelegramId(telegramId);

        return ownerShelters.getShelters().size();
    }

    public OwnerShelters getOwnerShelterById(long id) {
        return ownerSheltersRepository.findById(id).orElseThrow();
    }

    public List<Shelters> getPageShelters(int page, int size, long id) {
        if (page < 1){
            log.error("Page number is less than 1");
            throw new IllegalArgumentException("page must be greater than 0");
        }

        OwnerShelters ownerShelters = getOwnerShelterByTelegramId(id);

        List<Shelters> shelters = new ArrayList<>(ownerShelters.getShelters());

        int i = (page * size) - size;

        if (i > shelters.size() - 1){
            return new ArrayList<>();
        }

        int maxPageSize = page * size;

        List<Shelters> newShelters = new ArrayList<>();

        while (i < maxPageSize && i < shelters.size()){
            newShelters.add(shelters.get(i));
            i++;
        }

        return newShelters;
    }
}
